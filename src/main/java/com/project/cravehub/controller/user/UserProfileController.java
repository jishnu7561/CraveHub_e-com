package com.project.cravehub.controller.user;

import com.lowagie.text.DocumentException;
import com.project.cravehub.dto.AddressDto;
import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.*;
import com.project.cravehub.repository.*;
import com.project.cravehub.service.InvoiceService;
import com.project.cravehub.service.addressService.AddressService;
import com.project.cravehub.service.couponService.CouponService;
import com.project.cravehub.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ReviewRepository reviewRepository;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return  new UserRegistrationDto();
    }

    @ModelAttribute("address")
    public AddressDto addressDto() {
        return  new AddressDto();
    }

    @ModelAttribute("user_address")
    public AddressDto addressUpdateDto() {
        return new AddressDto();
    }

    @GetMapping("/profile")
    public String showProfileGet(Model model, Principal principal, HttpSession session) {
        String user_email = principal.getName();
        System.out.println("name of authenticated "+user_email);
        User user = userRepository.findByEmail(user_email);
        model.addAttribute("user",user);
        Wallet wallet = user.getWallet();
        model.addAttribute("wallet",wallet);
        List<Address> addresses = addressRepository.findByUserId(user);
        model.addAttribute("addresses",addresses);
        int cartCount = (int)session.getAttribute("cartCount");
        model.addAttribute("cartCount",cartCount);
        model.addAttribute("userName",(String)session.getAttribute("userName"));
        model.addAttribute("referralSuccess",(String)session.getAttribute("referralSuccess"));
        model.addAttribute("referralError",(String)session.getAttribute("referralError"));
        session.removeAttribute("referralSuccess");
        session.removeAttribute("referralError");

        model.addAttribute("profileSuccess",(String)session.getAttribute("profileSuccess"));
        model.addAttribute("profileError",(String)session.getAttribute("profileError"));
        session.removeAttribute("profileSuccess");
        session.removeAttribute("profileError");
        return "profile";
    }

    @PostMapping("/updateUserDetails")
    public String updateUserDetailsPost(Principal principal, @ModelAttribute("user")
                                            UserRegistrationDto userDto,HttpSession session) {

        String user_email = principal.getName();
        User user = userRepository.findByEmail(user_email);
        if(user!=null) {
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setUserName(userDto.getUserName());
            user.setEmail(userDto.getEmail());
            userRepository.save(user);
            session.setAttribute("profileSuccess","User details edited successfully.");
        }
        else {
            session.setAttribute("profileError","Failed to edit user details.");
        }
        return "redirect:/profile";
    }

    @PostMapping("/addAddress")
    public String addAddressPost(@ModelAttribute("address") AddressDto addressDto, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if(user!=null) {
            Address address = new Address();
            address.setUserId(user);
            address.setFullName(addressDto.getFullName());
            address.setCity(addressDto.getCity());
            address.setCountry(addressDto.getCountry());
            address.setState(addressDto.getState());
            address.setPinCode(addressDto.getPinCode());
            address.setNumber(addressDto.getNumber());
            addressRepository.save(address);
            return "redirect:/profile";
        }
        return "redirect:/profile?cantAdd";
    }

    @PostMapping("/updateAddress{id}")
    public String updateAddressPost(@PathVariable("id") Integer addressId,
                                    @ModelAttribute("user_address") AddressDto addressDto) {

        Address address = addressRepository.findById(addressId).orElse(null);
        if(address != null) {
            address.setFullName(addressDto.getFullName());
            address.setCity(addressDto.getCity());
            address.setCountry(addressDto.getCountry());
            address.setState(addressDto.getState());
            address.setPinCode(addressDto.getPinCode());
            address.setNumber(addressDto.getNumber());
            addressRepository.save(address);
            return "redirect:/profile";
        }
        return "redirect:/profile?addressNull";
    }

    @PostMapping("/updatePassword")
    public String changePasswordInProfilePost(@RequestParam("newPassword") String password,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                              Principal principal,HttpSession session) {
        String user_email = principal.getName();
        User user = userRepository.findByEmail(user_email);
        if(user == null) {
            return "user not found";
        }
        if(password.equals(confirmPassword)) {
            user.setPassword(passwordEncoder.encode(confirmPassword));
            userRepository.save(user);
            session.setAttribute("profileSuccess","Password changed successfully.");
        }
        else {
            session.setAttribute("profileError","Failed to change password,mismatch in given password.");
        }
        return "redirect:/profile";
    }

    @PostMapping("/deleteAddress")
    @ResponseBody
    public ResponseEntity<String> deleteAddress(@RequestParam("addressId") Integer address_id) {
        System.out.println("in----------------");
        if(addressService.isAddressInPurchaseOrder(address_id))
        {
            return ResponseEntity.ok("you cant delete this address,you can update.");

        }
            Address deleteAddress = addressService.deleteAddressById(address_id);
        if(deleteAddress != null)
        {
            return ResponseEntity.ok("Address deleted successfully.");

        }
        return ResponseEntity.status(500).body("Failed to delete address.");
    }

    @GetMapping("/orders")
    public String orders(Principal principal,Model model,HttpSession session) {
        User user = userRepository.findByEmail(principal.getName());
        model.addAttribute("userName",user.getUserName());
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByUser(user);
        Collections.reverse(purchaseOrders);
        model.addAttribute("purchaseOrder",purchaseOrders);
        int cartCount = (int)session.getAttribute("cartCount");
        model.addAttribute("cartCount",cartCount);
        model.addAttribute("reviewSuccessful",(String)session.getAttribute("reviewSuccessful"));
        session.removeAttribute("reviewSuccessful");
        model.addAttribute("reviewError",(String)session.getAttribute("reviewError"));
        session.removeAttribute("reviewError");
        return "orders";
    }

    @GetMapping("/cancelOrder")
    public String cancelOrderItem(@RequestParam(name = "orderItemId") Integer orderItemId) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);
        if(orderItemOptional.isPresent())
        {
            OrderItem orderItem = orderItemOptional.get();
            orderItem.setOrderStatus("cancelled");
            if(orderItem.getOrder().getPaymentStatus().equals("success")) {
                userService.addTransactionAndRefund(orderItem);
            }
            orderItemRepository.save(orderItem);
            return "redirect:/orders";
        }
        return "redirect:/orders?error";
    }

    @GetMapping("/downloadInvoice")
    public ResponseEntity<byte[]> downloadInvoice(@RequestParam("orderItemId") OrderItem orderItem) throws DocumentException, IOException {
        byte[] pdfBytes = invoiceService.generateInvoice(orderItem);
        System.out.println("called-------invoice");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    @GetMapping("/offers")
    public String showOffers(Model model,HttpSession session,Principal principal) {
        LocalDate date = LocalDate.now();
        List<Coupon> couponList = couponService.getAllValidCoupons(date);
        User user = userRepository.findByEmail(principal.getName());
        model.addAttribute("couponList",couponList);
        model.addAttribute("currentUser",user);
        model.addAttribute("cartCount",(int)session.getAttribute("cartCount"));
        return "offers";
    }

    @GetMapping("/wallet")
    public String showWallet(Principal principal,Model model,HttpSession session) {
        User user = userRepository.findByEmail(principal.getName());
        model.addAttribute("userName",user.getUserName());
        model.addAttribute("cartCount",(int)session.getAttribute("cartCount"));
        Wallet wallet = user.getWallet();
        Set<Transactions> transactions = user.getTransaction();
        for (Transactions transactions1 : transactions)
        {
            System.out.println("transaction are  "+transactions1);
        }
        if(wallet != null)
        {
            model.addAttribute("wallet",wallet);
            model.addAttribute("transaction",transactions);
        }
        return "wallet";
    }

    @GetMapping("/redeemOffer")
    public String redeemReferralOffer(@RequestParam("referral") String referralCode,
                                      Principal principal,HttpSession session)
    {
        System.out.println(referralCode);
        boolean verifyReferralCode = userService.verifyReferralCode(referralCode,principal.getName());
        if(verifyReferralCode)
        {
            session.setAttribute("referralSuccess","Congratulations! Your referral was successful, and your rewards are on the way");
        }
        else {
            session.setAttribute("referralError", "Oops! It seems there was an issue processing your referral. Please double-check the details and try again");
        }
        return "redirect:/profile";
    }

    @PostMapping("referralLink")
    public  String referralLink(@RequestParam("email") String email,
                                Principal principal,HttpSession session) throws MessagingException {
        userService.sentReferralLink(email,principal);
        session.setAttribute("profileSuccess","Referral Link successfully sent.");
        return "redirect:/profile";
    }
}
