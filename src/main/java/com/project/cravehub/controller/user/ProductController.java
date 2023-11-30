package com.project.cravehub.controller.user;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.Review;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.productservice.ProductService;
import com.project.cravehub.service.reviewService.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
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

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/singleProduct/{productId}")
    public String singleProduct(@PathVariable("productId") Integer productId, Model model,
                                Principal principal, HttpSession session) {
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
            int cartCount = (int) session.getAttribute("cartCount");
            model.addAttribute("cartCount",cartCount);
            model.addAttribute("user", user);
            model.addAttribute("userName",session.getAttribute((String)session.getAttribute("userName")));
            int rating = reviewService.getReviewCountForProduct(product);
            model.addAttribute("rating",rating);
            Integer sold = productService.totalSold(product,user);
            model.addAttribute("sold",sold);
            List<Review> reviewList = reviewService.getReviewsByProduct(product);
            model.addAttribute("review",reviewList);
        }

        return "product-single";
    }
}
