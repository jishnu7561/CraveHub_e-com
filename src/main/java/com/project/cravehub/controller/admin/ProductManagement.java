package com.project.cravehub.controller.admin;

import com.project.cravehub.dto.CategoryDto;
import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.admin.SubCategory;
import com.project.cravehub.repository.CategoryRepository;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.SubCategoryRepository;
import com.project.cravehub.service.productservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class ProductManagement {

    public static String uploadDir = System.getProperty("user.dir")+"/src/main/resources/static/productImages";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductService productService;
    @GetMapping("/listProducts")
    public String listProducts(Model model)
    {
        List<Product> products =  productRepository.findAll();
        model.addAttribute("products", products);
        return "list-products";
    }


    @GetMapping("/addProducts")
    public String addProducts(Model model) {
        List<Category> category = categoryRepository.findAll();
        model.addAttribute("categories",category);

        List<SubCategory> subCategory = subCategoryRepository.findAll();
        model.addAttribute("subCategories",subCategory);
        return "add-products";
    }

    @ModelAttribute("product")
    public ProductDto productDto()
    {
        return new ProductDto();
    }

    @PostMapping("/addProduct")
    public String addProducts(@ModelAttribute("product") ProductDto productDto, HttpSession session) {
        session.setAttribute("productDto", productDto);


        //productService.save(productDto);
        return "redirect:/admin/addProducts2";
    }

    @GetMapping("/addProducts2")
    public String addProducts2(Model model,HttpSession session) {
        ProductDto storedProductDto = (ProductDto) session.getAttribute("productDto");
        System.out.println(storedProductDto.getCategories());
        Category category = storedProductDto.getCategories();
        int id = category.getId();
        System.out.println("Category ID: " + id);
        List<SubCategory> subCategory = subCategoryRepository.findAll();
        model.addAttribute("subCategories",subCategory);
        model.addAttribute("categoryId",id);
        return "addProducts2";
    }

    @ModelAttribute("product2")
    public ProductDto productDto2()
    {
        return new ProductDto();
    }

    @PostMapping("/addProduct2")
    public String addProducts2(@ModelAttribute("product2") ProductDto productDto2, HttpSession session) throws IOException{
        ProductDto storedProductDto = (ProductDto) session.getAttribute("productDto");
        storedProductDto.setQuantity(productDto2.getQuantity());
        storedProductDto.setImageName(productDto2.getImageName());
        storedProductDto.setSubcategories(productDto2.getSubcategories());
        storedProductDto.setPrice(productDto2.getPrice());


        System.out.println("Stored Product DTO: " + storedProductDto.getImageName());

        System.out.println(productDto2.getQuantity());
        System.out.println(storedProductDto.getSubcategories());
        System.out.println(productDto2.getPrice());

        productService.save(storedProductDto);
        System.out.println("product are stored");

        return "redirect:/admin/addProducts";
    }


    @GetMapping("/updateProducts")
    public String updateProducts() {
        return "update-products";
    }

    @GetMapping("/deleteProduct/{productId}")
    public String deleteCategory(@PathVariable(value = "productId") Integer productId)
    {
        boolean isExist = productService.isExist(productId);
        if(isExist)
        {
            return "redirect:/admin/listProducts?cantDelete";
        }

        Product deleteProduct = productService.deleteProductById(productId);
        if(deleteProduct!=null) {
            return "redirect:/admin/listProducts";
        }
        return "redirect:/admin/listProducts?notExist";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable(value = "id") Integer productId, Model model) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            model.addAttribute("product", product);
            model.addAttribute("productId", productId);
            return "update-products";
        } else {
            return "redirect:/admin/listProducts?error";
        }
    }


    @PostMapping("/editProduct/{id}")
    public String editProduct(@PathVariable("id") Integer productId ,@ModelAttribute("product") ProductDto productDto)
    {
        productService.editProductByID(productId,productDto);
        return "redirect:/admin/listProducts";
    }


}
