package com.project.cravehub.controller.user;

import com.project.cravehub.dto.OtpDto;
import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.userservice.UserService;
import com.project.cravehub.util.EmailUtil;
import com.project.cravehub.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller

public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto registrationDto() {
        return new UserRegistrationDto();
    }


    @GetMapping("/registration")
    public String ShowRegistrationForm(HttpSession session, Model model) {
        model.addAttribute("reg",(String)session.getAttribute("regError"));
        session.removeAttribute("regError");
        return "signup";
    }


    @PostMapping("/registration")
    public String registerUserAccount(@RequestParam(name = "referralCode", required = false) String referralCode,
                                      @ModelAttribute("user") UserRegistrationDto userRegistrationDto,
                                      HttpSession session) {
        String email = userRegistrationDto.getEmail();

        session.setAttribute("referralCode",referralCode);

        // Check if the email already exists
        boolean validEmailId = userService.isValidEmailId(email);

        if (!validEmailId) {
            session.setAttribute("regError","You have already an account,please login");
            return "redirect:/registration";
        }
        else {
            // Register the user
            User verifyCustomer = userService.save(userRegistrationDto);
            userService.register(userRegistrationDto);
            System.out.println("service" + email);
            // Store the email in the session
            session.setAttribute("validEmailId", email);
            session.setAttribute("verifyCustomer", verifyCustomer);
            session.setAttribute("registration","Congratulations , you have successfully completed the 1 step for account creation.");

            return "redirect:/verify-account";
        }
    }
}
