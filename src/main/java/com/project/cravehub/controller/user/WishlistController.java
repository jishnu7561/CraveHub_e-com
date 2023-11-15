package com.project.cravehub.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.User;
import com.project.cravehub.model.user.Wishlist;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.repository.WishlistRepository;
import com.project.cravehub.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class WishlistController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @GetMapping("/wishlist")
    public String showWishlist(Model model,Principal principal,HttpSession session) {
        User user = userRepository.findByEmail(principal.getName());
        Wishlist wishlist = user.getWishlist();
        if(wishlist != null) {
            List<Product> productList = wishlist.getProducts();
            model.addAttribute("productList", productList);
        }
        model.addAttribute("cartCount",(Integer)session.getAttribute("cartCount"));
        return "wishlist";
    }

    @PostMapping("/addToWishlist")
    public ResponseEntity<String> addToWishlist(@RequestBody ProductDto productDTO,
                                                 Principal principal, HttpSession session) {
        Optional<Product> productOptional = productRepository.findById(productDTO.getProductId());
        Product product = new Product();
        if(productOptional.isPresent())
        {
            product = productOptional.get();
        }
        Map<String, String> response = new HashMap<>();
        boolean addedToWishlist = userService.addProductToWishlist(principal,productDTO);
        if(addedToWishlist) {
            return ResponseEntity.ok("added");
        }
        else {
            return ResponseEntity.ok("exist");
        }
    }

    @GetMapping("/deleteFromWishlist/{id}")
    public String deleteFromWishlist(@PathVariable("id") Product productId,Principal principal) {
        userService.deleteFromWishlist(productId,principal.getName());
        return "redirect:/wishlist";
    }
}
