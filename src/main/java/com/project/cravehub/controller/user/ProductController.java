package com.project.cravehub.controller.user;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.productservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/singleProduct/{productId}")
    public String singleProduct(@PathVariable("productId") Integer productId, Model model, Principal principal) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product",product);
        List<Product> products = productRepository.findAll();
        model.addAttribute("products",products);
        if (principal == null || principal.getName() == null) {
            // User is not authenticated, handle it accordingly.
            model.addAttribute("user", null); // or handle it in a way that fits your application logic
        } else {
            // User is authenticated, fetch user details and add to the model.
            String user_email = principal.getName();
            User user = userRepository.findByEmail(user_email);
            model.addAttribute("user", user);
        }

        return "product-single";
    }
}
