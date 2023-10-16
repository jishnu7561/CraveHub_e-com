package com.project.cravehub.controller.user;

import com.project.cravehub.dto.OtpDto;
import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.userservice.UserService;
import com.project.cravehub.util.EmailUtil;
import com.project.cravehub.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public String showOtp(){
        return "otp";
    }

    @ModelAttribute("otp")
    public OtpDto otpDto() {
        return new OtpDto();
    }


    @PostMapping("/verify-account")
    public String verificationPage(@ModelAttribute("otp") OtpDto otpDto, HttpServletRequest request, HttpSession session) {
        String result = otpDto.getA()+otpDto.getB()+otpDto.getC()+otpDto.getD()+otpDto.getE()+otpDto.getF();
        String emailid = session.getAttribute("validEmailId").toString();
        boolean flag = userService.verifyAccount(emailid,result);
        if(flag)
        {
            User verifyCustomer = (User) session.getAttribute("verifyCustomer");
            userRepository.save(verifyCustomer);
            System.out.println("successfully verified and regisrered");
            return "redirect:/login";
        }
        return "redirect:/verify-account?error";
    }


    @PostMapping("/regenerate-otp")
    public String regenerateOtp(HttpServletRequest request)
    {
        System.out.println("regenerate otp");
        String email = (String) request.getSession().getAttribute("validEmailId");
        System.out.println(email+"     in session");
        userService.regenerateOtp(email);
        System.out.println("otp regeneration successfull");
        return "redirect:/verify-account";
    }


}


