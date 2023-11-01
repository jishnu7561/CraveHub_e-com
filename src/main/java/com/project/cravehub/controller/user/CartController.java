package com.project.cravehub.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.Cart;
import com.project.cravehub.model.user.CartItem;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.CartItemRepository;
import com.project.cravehub.repository.CartRepository;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.service.productservice.ProductService;
import com.project.cravehub.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private ProductService productService;

    @Autowired
    private CartItemRepository cartItemRepository;
    @GetMapping("/cart")
    public String showCart(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByEmail(username);

        List<CartItem> cartItems = cartItemRepository.findByCartUser(user);
        model.addAttribute("cartItems",cartItems);

        model.addAttribute("userName",user.getUserName());

        double totalPrice = userService.totalPrice(username);
        model.addAttribute("totalPrice",totalPrice);

        return "cart";
    }

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestBody ProductDto productDTO, Principal principal) {
        Optional<Product> productOptional = productRepository.findById(productDTO.getProductId());
        Product product = productOptional.get();
        boolean productExist = userService.addProductToCart(principal,productDTO);
        Map<String, Object> response = new HashMap<>();
        if(product.getQuantity() == 0)
        {
            response.put("outOfStock",true);
        }
        else if(productExist) {
            product.setQuantity(product.getQuantity()-1);
            productRepository.save(product);
            response.put("added",true);
        }
        else {
            response.put("productExist", true);
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            return ResponseEntity.ok(jsonResponse);
        } catch (JsonProcessingException e) {
            // Handle the exception, e.g., log it and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response");
        }
    }

    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@RequestParam("count") int count, @RequestParam("productId") Product productId,Principal principal) {

        // Update the quantity in the database using your service method
        System.out.println("in controller");
        Map<String, Object> response = new HashMap<>();
            if(productId.getQuantity()-count < 0)
            {
                response.put("success", false);
            }
            else {
                String username = principal.getName();
                int updatedQuantity = userService.updateQuantity(count, productId.getProductId(), username);
                // Return the updated quantity as a response
                double totalPrice = userService.totalPrice(username);
                Optional<Product> productOptional = productRepository.findById(productId.getProductId());
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    product.setQuantity(product.getQuantity() - count);
                    productRepository.save(product);
                }
                int productQuantity = productOptional.get().getQuantity();
                System.out.println("---------------------" + productQuantity);

                response.put("success", true);
                response.put("updatedQuantity", updatedQuantity);
                response.put("totalPrice", totalPrice);


                System.out.println(updatedQuantity);

            }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);
            return ResponseEntity.ok(jsonResponse);
        } catch (JsonProcessingException e) {
            // Handle the exception, e.g., log it and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON response");
        }
    }

    @GetMapping("/deleteCart/{cartItemId}")
    public String deleteCartById(@PathVariable Integer cartItemId) {
         Optional<CartItem> isExist = cartItemRepository.findById(cartItemId);
         if(isExist.isPresent()) {
             int quantity = isExist.get().getQuantity();
             Optional<Product> productOptional = productRepository.findById(isExist.get().getProduct().getProductId());
             if(productOptional.isPresent()) {
                 Product product = productOptional.get();
                 product.setQuantity(product.getQuantity() + quantity);
                 productRepository.save(product);
                 cartItemRepository.deleteById(cartItemId);
                 return "redirect:/cart";
             }
         }
         return "redirect:/cart?error=NotExist";
    }
}
