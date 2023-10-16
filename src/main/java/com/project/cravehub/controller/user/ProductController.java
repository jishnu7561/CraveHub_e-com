package com.project.cravehub.controller.user;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.service.productservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/singleProduct/{productId}")
    public String singleProduct(@PathVariable("productId") Integer productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product",product);
        List<Product> products = productRepository.findAll();
        model.addAttribute("products",products);

        return "product-single";
    }
}
