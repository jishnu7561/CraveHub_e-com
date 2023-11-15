package com.project.cravehub.controller.admin;

import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.admin.SubCategory;
import com.project.cravehub.repository.CategoryRepository;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.SubCategoryRepository;
import com.project.cravehub.service.productservice.ProductService;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class ProductManagement {

    private static final String UPLOAD_DIR = "D:\\project\\cravehub\\src\\main\\resources\\static\\productImages\\";

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
        if( storedProductDto == null )
        {
            model.addAttribute("error", "please start from the beginning step");
            return "redirect:/admin/addProducts";
        }
        System.out.println(storedProductDto.getCategories());
        Category category = storedProductDto.getCategories();
        int id = category.getId();
        System.out.println("Category ID: " + id);
        List<SubCategory> subCategory = subCategoryRepository.findAll();
        model.addAttribute("subCategories",subCategory);
        model.addAttribute("categoryId",id);
        Product product = new Product();
        model.addAttribute("product",product);
        return "addProducts2";
    }

    @ModelAttribute("product2")
    public ProductDto productDto2()
    {
        return new ProductDto();
    }


//    @PostMapping("/addProduct2")
//    public String addProducts2(@ModelAttribute("product2") ProductDto productDto2, HttpSession session,
//                               @RequestParam("image") MultipartFile file,Model model) throws IOException{
//        ProductDto storedProductDto = (ProductDto) session.getAttribute("productDto");
//        if( storedProductDto == null )
//        {
//            model.addAttribute("error", "please start from the beginning step");
//            return "redirect:/admin/addProducts";
//        }
//        storedProductDto.setQuantity(productDto2.getQuantity());
//        storedProductDto.setSubcategories(productDto2.getSubcategories());
//        storedProductDto.setPrice(productDto2.getPrice());
//        try {
//            // Validate uploaded file using Apache Tika
//            Tika tika = new Tika();
//            String mimeType = tika.detect(file.getInputStream());
//            if (!mimeType.startsWith("image")) {
//                model.addAttribute("error", "Invalid file format. Please upload an image.");
//                return "list-products";
//            }
//            // Read the uploaded image into a BufferedImage
//            BufferedImage originalImage = ImageIO.read(file.getInputStream());
//
////            if (originalImage == null) {
////                System.out.println("Failed to read the uploaded image. Check if the uploaded file is a valid image.");
////                // Handle this case, maybe return an error response to the user
////                return "redirect:/admin/addProducts?error"; // Or redirect to an error page
////
//            // Define crop dimensions (adjust these according to your requirements)
//            int cropX = 50;
//            int cropY = 20;
//            int cropWidth = 500;
//            int cropHeight = 300;
//
//            // Crop the image
//            BufferedImage croppedImage = originalImage.getSubimage(cropX, cropY, cropWidth, cropHeight);
//
//            // Save the cropped image to a file
//            String croppedImageFileName =storedProductDto.getProductName()+"_cropped.jpg"; // You can generate a unique filename if needed
//            File croppedImageFile = new File(UPLOAD_DIR, croppedImageFileName);
//            ImageIO.write(croppedImage, "jpg", croppedImageFile);
//
//            // Set the cropped image file path in your ProductDto or store it in the database as needed
//            storedProductDto.setImageName("/productImages/" + croppedImageFileName);
//
//            System.out.println("Cropped Image Path: " + storedProductDto.getImageName());
//            // Delete the temporary file
//            File tempFile = File.createTempFile("temp", ".jpg");
//            tempFile.deleteOnExit();
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Handle exceptions
//        }
//        productService.save(storedProductDto);
//
//        return "redirect:/admin/addProducts";
//    }


//   ######################## cropper.js######################################

    @PostMapping("/addProduct2")
    public String addProducts2(@ModelAttribute("product2") ProductDto productDto, HttpSession session,
                               @RequestParam("croppedImage") String croppedImageData, Model model) throws IOException {
        // ... (your existing code)


        System.out.println("cropped image Name from product2 controller ="+ croppedImageData);
        ProductDto storedProductDto = (ProductDto) session.getAttribute("productDto");
        if( storedProductDto == null )
        {
            model.addAttribute("error", "please start from the beginning step");
            return "redirect:/admin/addProducts";
        }
        storedProductDto.setQuantity(productDto.getQuantity());
        storedProductDto.setSubcategories(productDto.getSubcategories());
        storedProductDto.setPrice(productDto.getPrice());
        // Read the uploaded image into a BufferedImage
        byte[] decodedImageData = Base64.getDecoder().decode(croppedImageData.split(",")[1]); // Remove the data:image/png;base64 prefix
        BufferedImage croppedImage = ImageIO.read(new ByteArrayInputStream(decodedImageData));

        // Save the cropped image to a file (you can generate a unique filename if needed)
        String croppedImageFileName = storedProductDto.getProductName()+".jpg";
        File croppedImageFile = new File(UPLOAD_DIR, croppedImageFileName);
//        ImageIO.write(croppedImage, "jpg", croppedImageFile);
        FileUtils.writeByteArrayToFile(croppedImageFile, decodedImageData);

        // Set the cropped image file path in your ProductDto or store it in the database as needed
        storedProductDto.setImageName("/productImages/" + croppedImageFileName);

        // ... (your existing code)
        productService.save(storedProductDto);

        return "redirect:/admin/addProducts";
    }


//   ######################## cropper.js#####################################

    @GetMapping("/updateProducts")
    public String updateProducts() {
        return "update-products";
    }

//    @GetMapping("/deleteProduct/{productId}")
//    public String deleteCategory(@PathVariable(value = "productId") Integer productId)
//    {
//        boolean isExist = productService.isExist(productId);
//        if(isExist)
//        {
//            return "redirect:/admin/listProducts?cantDelete";
//        }
//        Product deleteProduct = productService.deleteProductById(productId);
//        if(deleteProduct!=null) {
//            return "redirect:/admin/listProducts";
//        }
//        return "redirect:/admin/listProducts?notExist";
//    }

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

//    @PostMapping("/editProduct/{id}")
//    public String editProduct(@PathVariable("id") Integer productId ,@ModelAttribute("product") ProductDto productDto)
//    {
//        productService.editProductByID(productId,productDto, croppedImageFileName);
//        return "redirect:/admin/listProducts";
//    }

    @PostMapping("/editProduct/{id}")
    public String editProduct(@PathVariable("id") Integer productId ,@ModelAttribute("product") ProductDto productDto,
                               @RequestParam("image") MultipartFile file,Model model) throws IOException{

        String croppedImageFileName = productDto.getImageName();
        try {
            // Validate uploaded file using Apache Tika
            Tika tika = new Tika();
            String mimeType = tika.detect(file.getInputStream());
            if (!mimeType.startsWith("image")) {
                model.addAttribute("error", "Invalid file format. Please upload an image.");
                return "redirect:/admin/listProducts";
            }

            // Read the uploaded image into a BufferedImage
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
             croppedImageFileName = generateUniqueFileName(Objects.requireNonNull(file.getOriginalFilename()));

//            if (originalImage == null) {
//                System.out.println("Failed to read the uploaded image. Check if the uploaded file is a valid image.");
//                // Handle this case, maybe return an error response to the user
//                return "redirect:/admin/addProducts?error"; // Or redirect to an error page
//
            // Define crop dimensions (adjust these according to your requirements)
            int cropX = 50;
            int cropY = 20;
            int cropWidth = 500;
            int cropHeight = 300;

            // Crop the image
            BufferedImage croppedImage = originalImage.getSubimage(cropX, cropY, cropWidth, cropHeight);

            // Save the cropped image to a file
            // You can generate a unique filename if needed
            File croppedImageFile = new File(UPLOAD_DIR, croppedImageFileName);
            ImageIO.write(croppedImage, "jpg", croppedImageFile);
            croppedImageFileName= "/productImages/" +croppedImageFileName;
            // Set the cropped image file path in your ProductDto or store it in the database as needed
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions
        }
        productService.editProductByID(productId,productDto,croppedImageFileName );
        return "redirect:/admin/listProducts";
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        return uniqueFileName;
    }



    @GetMapping("/blockProduct/{productId}")
    public String blockSubCategory(@PathVariable("productId") Integer id) {
        productService.blockProductById(id);
        return "redirect:/admin/listProducts";
    }

    @GetMapping("/unblockProduct/{productId}")
    public String unblockSubCategory(@PathVariable("productId") Integer id) {
        productService.unBlockProductById(id);
        return "redirect:/admin/listProducts";
    }
    @GetMapping("/test")
    public String test()
    {
        return "Test";
    }

}
