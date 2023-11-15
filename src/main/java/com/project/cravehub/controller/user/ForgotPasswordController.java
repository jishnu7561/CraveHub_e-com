package com.project.cravehub.controller.user;

import com.project.cravehub.dto.OtpDto;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

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
    @ResponseBody
    public Map<String, String> verifyOtpPost(@RequestParam("a") String a, @RequestParam("e") String e,
                                             @RequestParam("b") String b, @RequestParam("f") String f,
                                             @RequestParam("c") String c, @RequestParam("d") String d,
                                             HttpSession session, HttpServletRequest request) {
        String otp_entered = a+b+c+d+e+f;
        String emailId =  (String) request.getSession().getAttribute("email");
        System.out.println(emailId);
        boolean flag = userService.verifyAccount(emailId,otp_entered);
        Map<String, String> response = new HashMap<>();
        if(emailId == null) {
            response.put("valid","false");
            response.put("message","session have Expired,please try again!!");
//            return "redirect:/verifyOtp?sessionExpired";
        }
        else if(flag) {
            response.put("valid", "true");
            response.put("message", "successful");
            session.setAttribute("verification","successful");
//            return "redirect:/changePassword";
        }
        else{
                response.put("valid","true");
                response.put("message","Entered OTP is wrong!!");
//            return "redirect:/verifyOtp?otpWrong";
            }
        return response;
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
    public String changePasswordGet(HttpSession session) {
        String verify = (String)session.getAttribute("verification");
        if(verify != null) {
            session.removeAttribute("verification");
            return "changePassword";
        }
        return "redirect:/verifyOtp";
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
