package com.project.cravehub.controller.user;

import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.repository.CategoryRepository;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.service.categoryservice.CategoryService;
import com.project.cravehub.service.productservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShopController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/shop")
    public  String showShop(Model model, HttpSession session,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "12") int pageSize,
                            @RequestParam(name ="category",required=false) String category){

        String userName = (String) session.getAttribute("userName");
        model.addAttribute("userName ",userName);
        List<Category> categoryList = categoryRepository.findAll();
        productService.updateIsEnabled();
        categoryService.updateIsEnabled();
//        List<Product> productList = productService.findAllProducts();
        Page<Product> productsPage;
        if (category != null && !category.isEmpty()) {
            productsPage = productService.findProductsByCategory(category, page, pageSize);
            model.addAttribute("category",category);
        } else {
            productsPage = productService.findAllProducts(page, pageSize);
        }
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());
//        int cartCount = (Integer) session.getAttribute("cartCount");
//        model.addAttribute("cartCount",cartCount);
        Integer cartCount = (Integer) session.getAttribute("cartCount");
        int cartCountValue = (cartCount != null) ? cartCount.intValue() : 0;
        model.addAttribute("cartCount", cartCountValue);

        return "shop";
    }
    @GetMapping("/search")
    public String searchProducts(@RequestParam("searchTerm") String searchTerm,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "12") int pageSize,
                                 Model model,HttpSession session) {
        Page<Product> productsPage = productService.findProductsBySearchTerm(searchTerm, page, pageSize);
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        Integer cartCount = (Integer) session.getAttribute("cartCount");
        int cartCountValue = (cartCount != null) ? cartCount.intValue() : 0;
        model.addAttribute("cartCount", cartCountValue);
        return "shop";
    }
}
