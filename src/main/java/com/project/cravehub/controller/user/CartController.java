package com.project.cravehub.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.CartItem;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.CartItemRepository;
import com.project.cravehub.repository.CartRepository;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @GetMapping("/cart")
    public String showCart(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);
//        if(user != null) {
//        List<CartItem> cartItems = userService.getCartItemsOfUser(user.getId());
//        model.getAttribute("cartItems",cartItems);
//        return "cart";
//        }
        List<CartItem> cartItems = cartItemRepository.findByCartUser(user);
        model.addAttribute("cartItems",cartItems);

        double totalPrice = userService.totalPrice(username);
        model.addAttribute("totalPrice",totalPrice);

        return "cart";
    }

    @PostMapping("/addToCart")
    public ResponseEntity<Boolean> addToCart(@RequestBody ProductDto productDTO, Principal principal) {

       boolean productExist = userService.addProductToCart(principal,productDTO);


        return ResponseEntity.ok(productExist);

    }

    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@RequestParam("count") int count, @RequestParam("productId") Product productId,Principal principal) {

        // Update the quantity in the database using your service method
        System.out.println("in controller");

            String username = principal.getName();
            int updatedQuantity = userService.updateQuantity(count, productId.getProductId(), username);
            // Return the updated quantity as a response
            double totalPrice = userService.totalPrice(username);

            // Create a map to hold updated values
            Map<String, Object> response = new HashMap<>();
            response.put("updatedQuantity", updatedQuantity);
            response.put("totalPrice", totalPrice);

            System.out.println(updatedQuantity);
            //return ResponseEntity.ok(Integer.toString(updatedQuantity));
           // return ResponseEntity.ok(response.toString());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            return ResponseEntity.ok(jsonResponse);
        } catch (JsonProcessingException e) {
            // Handle the exception, e.g., log it and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response");
        }

    }
}
