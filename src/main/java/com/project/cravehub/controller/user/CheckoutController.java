package com.project.cravehub.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cravehub.dto.AddressDto;
import com.project.cravehub.model.user.*;
import com.project.cravehub.repository.*;
import com.project.cravehub.service.couponService.CouponService;
import com.project.cravehub.service.userservice.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
//import com.razorpay.Order;

@Controller
public class CheckoutController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @ModelAttribute("address")
    public AddressDto addressDto() {
        return new AddressDto();
    }


    @GetMapping("/checkout")
    public String showCheckoutGet(Principal principal, Model model, HttpSession session) {
        User user = userRepository.findByEmail(principal.getName());
        double totalAmount = userService.totalPrice(user.getEmail());
        model.addAttribute("totalAmount", totalAmount);
        System.out.println(totalAmount);
        model.addAttribute("userName", user.getUserName());
        List<Address> addresses = addressRepository.findByUserId(user);
        model.addAttribute("addresses", addresses);
        Address address = new Address(); // Create a new Address object or fetch it from your database
        model.addAttribute("address", address);
        int cartCount = (int)session.getAttribute("cartCount");
        model.addAttribute("cartCount",cartCount);
        if(user.getWallet()!= null) {
            model.addAttribute("balance", user.getWallet().getBalance());
        }
        return "checkout";
    }

    @GetMapping("/orderPlaced")
    public String orderPlaced(Principal principal, Model model,HttpSession session) {
        User user = userRepository.findByEmail(principal.getName());
        model.addAttribute("userName", user.getUserName());
        model.addAttribute("cartCount",session.getAttribute("cartCount"));
        return "OrderPlaced";
    }

    @PostMapping("/placeAnOrder")
    @ResponseBody
    public ResponseEntity<String> placeAnOrderPost(
            @RequestParam("address") Integer addressId,
            @RequestParam("optradio") String paymentMethod,
            @RequestParam("totalAmount") double totalAmount,
            @RequestParam(value = "couponId", required = false) Integer couponId,
            Principal principal,HttpSession session,Model model) throws RazorpayException {
            System.out.println("in place an order");

//        if(addressId == null)
//        {
//            return ResponseEntity.ok(false);
//        }
//        if(paymentMethod == null)
//        {
//            return ResponseEntity.ok(false);
//        }
        Map<String, Object> response = new HashMap<>();
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
        Cart cart = cartRepository.findByUser(user);
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        Coupon coupon = couponService.couponById(couponId);
        Optional<Address> addressOptional = addressRepository.findById(addressId);
        addressOptional.ifPresent(purchaseOrder::setAddress);
        Address address = addressOptional.get();
        if (coupon != null) {
            purchaseOrder.setCoupon(coupon);
            couponService.saveCouponAndUser(principal.getName(), coupon);
        }

        purchaseOrder.setOrderAmount(totalAmount);
        purchaseOrder.setOrderDate(LocalDateTime.now());

        purchaseOrder.setUser(user);
        purchaseOrder.setPaymentMethod(paymentMethod);
        if(paymentMethod.equals("wallet"))
        {
            userService.addBalanceToWallet(user,totalAmount);
            purchaseOrder.setPaymentStatus("success");
        }
        purchaseOrder.setAddress(address);
            if(paymentMethod.equals("COD")) {
                purchaseOrder.setPaymentStatus("pending");
            }
            response.put("isValid", true);
            purchaseOrderRepository.save(purchaseOrder);
            boolean added = userService.addOrderItems(cart, purchaseOrder);
            int cartItemCount = userService.getCartItemsCount(user);
            session.setAttribute("cartCount", cartItemCount);
            model.addAttribute("cartCount", cartItemCount);
            System.out.println("checkout ---------"+added);
        if(paymentMethod.equals("online")) {
            RazorpayClient razorpay = new RazorpayClient("rzp_test_wGwkqS0TUZJIEr", "0bbu25Q53eoTXXqWyF1gFiOT");
            userService.addTransaction(user,purchaseOrder);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", totalAmount * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt#1");
            JSONObject notes = new JSONObject();
            notes.put("notes_key_1", "Tea, Earl Grey, Hot");
            orderRequest.put("notes", notes);

            Order order = razorpay.orders.create(orderRequest);
            response.put("isValid", false);
            response.put("orderId", order.get("id")); // Get orderId from Razorpay Order object
            response.put("amount", order.get("amount"));
            response.put("purchaseId",purchaseOrder.getOrderId());
            response.put("email",username);
            response.put("username",address.getFullName());
            response.put("contact",address.getNumber());

        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            return ResponseEntity.ok(jsonResponse);
        } catch (
                JsonProcessingException e) {
            // Handle the exception, e.g., log it and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response");
        }
    }


    @PostMapping("/couponCode")
    @ResponseBody
    public ResponseEntity<String> verifyCouponCode(String couponCode, Principal principal) {
        String userEmail = principal.getName();
        Coupon coupon = couponRepository.findByCouponCode(couponCode.trim());
        double totalAmount = couponService.totalAmountPurchased(userEmail);

        LocalDate currentDate = LocalDate.now();

        boolean isCouponAlreadyUsed = couponService.isCouponAlreadyUsed(couponCode, userEmail);
        Map<String, Object> response = new HashMap<>();

        int usersCount = couponRepository.countByCouponCodeAndUserIsNotNull(couponCode);
        System.out.println(usersCount+" +++++++++++++++++++++++=");
        if (coupon == null) {
            // Coupon does not exist
            response.put("isValid", false);
            response.put("message", "Coupon not valid");
        } else if (!coupon.getIsActive()) {
            // Coupon is not active
            response.put("isValid", false);
            response.put("message", "Coupon not active");
        } else if (isCouponAlreadyUsed) {
            response.put("isValid", false);
            response.put("message", "You have already claimed this coupon");
        } else if (currentDate.isAfter(coupon.getExpiryDate())) {
            response.put("isValid", false);
            response.put("message", "Coupon is invalid");
        } else if (totalAmount < coupon.getMinimumPurchaseAmount()) {
            response.put("isValid", false);
            response.put("message", "The minimum spend has not been met");
        } else if (usersCount >= coupon.getUsageCount()) {
            response.put("isValid", false);
            response.put("message", "Coupon has reached the maximum usage limit");
        } else {
//            couponService.saveCouponAndUser(userEmail, coupon);
            response.put("isValid", true);
            response.put("couponAmount", coupon.getAmount());
            response.put("couponId", coupon.getCouponId());
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            return ResponseEntity.ok(jsonResponse);
        } catch (JsonProcessingException e) {
            // Handle the exception, e.g., log it and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response");
        }
    }

    @PostMapping("/checkout/addAddress")
    public String addAddressPost(@ModelAttribute("address") AddressDto addressDto, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if (user != null) {
            Address address = new Address();
            address.setUserId(user);
            address.setFullName(addressDto.getFullName());
            address.setCity(addressDto.getCity());
            address.setCountry(addressDto.getCountry());
            address.setState(addressDto.getState());
            address.setPinCode(addressDto.getPinCode());
            address.setNumber(addressDto.getNumber());
            addressRepository.save(address);
            return "redirect:/checkout";
        }
        return "redirect:/checkout?cantAdd";
    }

    @PostMapping("/verifyPayment")
    @ResponseBody
    public ResponseEntity<Boolean> verifyPayment(@RequestParam("orderId") String orderId,
                                                 @RequestParam("signature") String signature,
                                                 @RequestParam("paymentId") String paymentId,
                                                 @RequestParam("purchaseId") Integer purchaseId,
                                                 Principal principal) throws RazorpayException {

        RazorpayClient razorpay = new RazorpayClient("rzp_test_wGwkqS0TUZJIEr", "0bbu25Q53eoTXXqWyF1gFiOT");

        String secret = "0bbu25Q53eoTXXqWyF1gFiOT";

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", orderId);
        options.put("razorpay_payment_id", paymentId);
        options.put("razorpay_signature", signature);

        boolean status =  Utils.verifyPaymentSignature(options, secret);
        if(status)
        {
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseId).orElse(null);
            if(purchaseOrder != null)
            {
                String payment_status ="success";
                purchaseOrder.setPaymentStatus(payment_status);
                purchaseOrder.setTransactionId(paymentId);
                purchaseOrderRepository.save(purchaseOrder);
                System.out.println(purchaseOrder.getPaymentMethod());
                System.out.println(purchaseOrder.getPaymentStatus());
                System.out.println(purchaseOrder.getTransactionId());

            }
        }
        return ResponseEntity.ok(status);
    }

    @GetMapping("/couponRemove")
    public ResponseEntity<Integer> removeCoupon(Principal principal) {
        int totalAmount = (int) couponService.totalAmountPurchased(principal.getName());
        return ResponseEntity.ok(totalAmount);
    }
}