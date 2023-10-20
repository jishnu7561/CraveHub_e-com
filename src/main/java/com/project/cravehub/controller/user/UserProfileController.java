package com.project.cravehub.controller.user;

import com.project.cravehub.dto.AddressDto;
import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.Address;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.AddressRepository;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.addressService.AddressService;
import com.project.cravehub.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    public String addAddressPost(@ModelAttribute("address") AddressDto addressDto,Principal principal) {
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

    @GetMapping("/deleteAddress/{address_id}")
    public String deleteAddress(@PathVariable("address_id") Integer address_id) {
        Address deleteAddress = addressService.deleteAddressById(address_id);
        if(deleteAddress != null)
        {
            return "redirect:/profile";
        }
        return "redirect:/profile?addressNotFound";
    }

}
