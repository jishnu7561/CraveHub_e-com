package com.project.cravehub.controller.user;

import com.project.cravehub.dto.OtpDto;
import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.user.User;
import com.project.cravehub.model.user.Wallet;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.userservice.UserService;
import com.project.cravehub.util.EmailUtil;
import com.project.cravehub.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class OtpController {

    @Autowired
    private UserService userService;

@Autowired
private UserRepository userRepository;

//    ----------------------------------from main controller------------------------------
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private OtpUtil otpUtil;

    @Autowired
    private UserRegistrationDto registrationDto;

    @GetMapping("/verify-account")
    public String showOtp(HttpSession session,Model model){
        String registration = (String) session.getAttribute("registration");
        String email = (String)session.getAttribute("validEmailId");
        model.addAttribute("email",email);
        model.addAttribute("reg",registration);
        return "otp";
    }

    @ModelAttribute("otp")
    public OtpDto otpDto() {
        return new OtpDto();
    }

    @PostMapping("/verify-account")
    public ResponseEntity<Boolean> verificationPage(@RequestParam("a") String a, @RequestParam("e") String e,
                                                    @RequestParam("b") String b, @RequestParam("f") String f,
                                                    @RequestParam("c") String c, @RequestParam("d") String d,
                                                    HttpServletRequest request, HttpSession session, Model model) {
        String result = a+b+c+d+e+f;
        String emailid = session.getAttribute("validEmailId").toString();
        boolean flag = userService.verifyAccount(emailid,result);
        if(flag)
        {
            User verifyCustomer = (User) session.getAttribute("verifyCustomer");
            userService.createWallet(verifyCustomer);
            userService.createCartForUser(emailid);
            userRepository.save(verifyCustomer);
            userService.addReferralOffer(session,emailid);
            System.out.println("successfully verified and regisrered");
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }


    @GetMapping("/regenerate-otp")
    public String regenerateOtp( HttpServletRequest request)
    {
        System.out.println("regenerate otp");
        String email = (String) request.getSession().getAttribute("validEmailId");
        System.out.println(email+"     in session");
        userService.regenerateOtp(email);
        System.out.println("otp regeneration successfull");
        return "redirect:/verify-account";
    }


}


