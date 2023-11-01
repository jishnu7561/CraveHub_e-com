package com.project.cravehub.controller.user;

import com.lowagie.text.DocumentException;
import com.project.cravehub.dto.AddressDto;
import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.Address;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.model.user.PurchaseOrder;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.AddressRepository;
import com.project.cravehub.repository.OrderItemRepository;
import com.project.cravehub.repository.PurchaseOrderRepository;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.InvoiceService;
import com.project.cravehub.service.addressService.AddressService;
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

import java.io.IOException;
import java.security.Principal;
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
    public String showProfileGet(Model model, Principal principal) {
        String user_email = principal.getName();
        System.out.println("name of authenticated "+user_email);
        User user = userRepository.findByEmail(user_email);
        model.addAttribute("user",user);
        List<Address> addresses = addressRepository.findByUserId(user);
        model.addAttribute("addresses",addresses);
        return "profile";
    }

    @PostMapping("/updateUserDetails")
    public String updateUserDetailsPost(Principal principal, @ModelAttribute("user")
                                            UserRegistrationDto userDto) {

        String user_email = principal.getName();
        User user = userRepository.findByEmail(user_email);
        if(user!=null) {
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setUserName(userDto.getUserName());
            user.setEmail(userDto.getEmail());
            userRepository.save(user);
            return "redirect:/profile";
        }
        return "redirect:/profile?SmtngWrong";
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
                                     @RequestParam("confirmPassword") String confirmPassword,Principal principal) {
        String user_email = principal.getName();
        User user = userRepository.findByEmail(user_email);
        if(user == null) {
            return "user not found";
        }
        if(password.equals(confirmPassword)) {
            user.setPassword(passwordEncoder.encode(confirmPassword));
            userRepository.save(user);
            return "redirect:/profile";
        }
        return "redirect:/profile?mismatch";
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
    public String orders(Principal principal,Model model) {
        User user = userRepository.findByEmail(principal.getName());
        model.addAttribute("userName",user.getUserName());
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByUser(user);
        Collections.reverse(purchaseOrders);
        model.addAttribute("purchaseOrder",purchaseOrders);
        return "orders";
    }

    @GetMapping("/cancelOrder/{orderItemId}")
    public String cancelOrderItem(@PathVariable Integer orderItemId) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);
        if(orderItemOptional.isPresent())
        {
            OrderItem orderItem = orderItemOptional.get();
            orderItem.setOrderStatus("cancelled");
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

}
