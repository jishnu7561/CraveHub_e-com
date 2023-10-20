package com.project.cravehub.controller.user;

import com.project.cravehub.dto.UserRegistrationDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.productservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Bean
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRegistrationDto registrationDto;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String showHome(Model model, Principal principal) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products",products);

//        if(principal.getName()==null)
//        {
//            return "index";
//        }
//        String user_email = principal.getName();
//        User user = userRepository.findByEmail(user_email);
//        model.addAttribute("user",user);
//        return "index";

        if (principal == null || principal.getName() == null) {
            // User is not authenticated, handle it accordingly.
            model.addAttribute("user", null); // or handle it in a way that fits your application logic
        } else {
            // User is authenticated, fetch user details and add to the model.
            String user_email = principal.getName();
            User user = userRepository.findByEmail(user_email);
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        System.out.println("login page");
        return "login";
    }

    @GetMapping("/accessDenied")
    public String accessDenied()
    {
        return "accessDenied";
    }

}
