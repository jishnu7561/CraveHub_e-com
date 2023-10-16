package com.project.cravehub.controller.user;

import com.project.cravehub.dto.OtpDto;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @ModelAttribute("otp")
    public OtpDto otpDto() {
        return new OtpDto();
    }


    @GetMapping("/confirmEmail")
    public String confirmEmailGet() {
        return "forgotPasswordEmail";
    }

    @PostMapping("/confirmEmail")
    public String confirmEmailPost(@RequestParam("email") String email, HttpSession session) {
        User user = userRepository.findByEmail(email);
        if(user!= null) {
            session.setAttribute("email",email);
            userService.sentOtp(email);
            String emailId = (String) session.getAttribute("email");
            System.out.println(emailId);
            return "redirect:/verifyOtp";
        }
        return "redirect:/confirmEmail?emailNotExist";
    }

    @GetMapping("/verifyOtp")
    public String verifyOtpGet() {
        return "forgotPasswordOtp";
    }

    @PostMapping("/verifyOtp")
    public String verifyOtpPost(@ModelAttribute("otp") OtpDto otpDto, HttpSession session, HttpServletRequest request) {
        String otp_entered = otpDto.getA()+otpDto.getB()+otpDto.getC()+
                otpDto.getD()+otpDto.getE()+otpDto.getF();
        String emailId =  (String) request.getSession().getAttribute("email");
        System.out.println(emailId);
        if(emailId == null) {
            return "redirect:/verifyOtp?sessionExpired";
        }
        boolean flag = userService.verifyAccount(emailId,otp_entered);
        try{
            if(flag){
                return "redirect:/changePassword";
            }
            else{
                return "redirect:/verifyOtp?otpWrong";
            }
        }
        catch (Exception e){
            return "error";
        }
    }

    @PostMapping("/regenerateForgotOtp")
    public String regenerateForgotOtp(HttpServletRequest request)
    {
        System.out.println("regenerate otp");
        String email = (String) request.getSession().getAttribute("email");
        System.out.println(email+"     in session");
        userService.regenerateOtp(email);
        System.out.println("otp regeneration successful");
        return "redirect:/verifyOtp";
    }

    @GetMapping("/changePassword")
    public String changePasswordGet() {
        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String changePasswordPost(@RequestParam("password") String password,@RequestParam("confirmPassword") String confirmPassword,HttpServletRequest request) {
        String emailId =  (String) request.getSession().getAttribute("email");
        User user = userRepository.findByEmail(emailId);
        if(user == null)
        {
            return "user not found";
        }
        if(password.equals(confirmPassword))
        {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return "redirect:/login";
        }
        return "redirect:/changePassword?mismatch";
    }
}
