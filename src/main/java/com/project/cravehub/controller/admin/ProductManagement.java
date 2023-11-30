package com.project.cravehub.controller.admin;

import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.admin.ProductImages;
import com.project.cravehub.model.admin.SubCategory;
import com.project.cravehub.repository.CategoryRepository;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.ProductsImageRepository;
import com.project.cravehub.repository.SubCategoryRepository;
import com.project.cravehub.service.productservice.ProductService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private ProductsImageRepository productsImageRepository;
    @GetMapping("/listProducts")
    public String listProducts(Model model)
    {
        List<Product> products =  productRepository.findAll();
        model.addAttribute("products", products);
        return "list-products";
    }


    @GetMapping("/addProducts")
    public String addProductsGet(Model model,HttpSession session) {
        List<Category> category = categoryRepository.findAll();
        model.addAttribute("categories",category);

        List<SubCategory> subCategory = subCategoryRepository.findAll();
        model.addAttribute("subCategories",subCategory);

        model.addAttribute("addProductError",(String)session.getAttribute("addProductError"));
        session.removeAttribute("addProductError");
        return "add-products";
    }

    @ModelAttribute("product")
    public ProductDto productDto()
    {
        return new ProductDto();
    }

    @PostMapping("/addProduct")
    public String addProductsPost(@ModelAttribute("product") ProductDto productDto,
                                  HttpSession session) {

        boolean isAlreadyExist = productService.findProductNameExist(productDto.getProductName().trim());
        if(isAlreadyExist) {
            System.out.println(productDto.getProductName() +" already exist");
            session.setAttribute("addProductError","Product Name already exist,please change the name .");
            return "redirect:/admin/addProducts";
        }
        else {
            System.out.println(productDto.getProductName());
            session.setAttribute("productDto", productDto);
        }
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



//    ===========================  add product using cropper.js  ============================

//    @PostMapping("/addProduct2")
//    public String addProducts2(@ModelAttribute("product2") ProductDto productDto,
//                               HttpSession session,
//                               @RequestParam("croppedImage1") MultipartFile croppedImage1,
//                               @RequestParam("croppedImage2") MultipartFile croppedImage2,
//                               @RequestParam("croppedImage3") MultipartFile croppedImage3,
//                               @RequestParam("croppedImage4") MultipartFile croppedImage4,
//                               Model model) throws IOException {
//
//        ProductDto storedProductDto = (ProductDto) session.getAttribute("productDto");
//        if (storedProductDto == null) {
//            model.addAttribute("error", "please start from the beginning step");
//            return "redirect:/admin/addProducts";
//        }
//
//        ProductImages productImage = new ProductImages();
//        handleCroppedImage(croppedImage1, storedProductDto, "1",productImage);
//        handleCroppedImage(croppedImage2, storedProductDto, "2",productImage);
//        handleCroppedImage(croppedImage3, storedProductDto, "3",productImage);
//        handleCroppedImage(croppedImage4, storedProductDto, "4",productImage);
//
//        storedProductDto.setQuantity(productDto.getQuantity());
//        storedProductDto.setSubcategories(productDto.getSubcategories());
//        storedProductDto.setCategories(productDto.getCategories());
//        storedProductDto.setPrice(productDto.getPrice());
//        Product productEntity =  productService.save(storedProductDto);
//
//        productImage.setProduct(productEntity);
//
//
//        return "redirect:/admin/addProducts";
//    }
//
//    private void handleCroppedImage(MultipartFile croppedImageData, ProductDto storedProductDto, String suffix,ProductImages productImage) throws IOException {
//
//        if (croppedImageData != null && !croppedImageData.isEmpty()) {
//            byte[] decodedImageData = croppedImageData.getBytes();
//            BufferedImage croppedImage = ImageIO.read(new ByteArrayInputStream(decodedImageData));
//            String croppedImageFileName = storedProductDto.getProductName().trim() + "_" + suffix + ".jpg";
//            File croppedImageFile = new File(UPLOAD_DIR, croppedImageFileName);
//            FileUtils.writeByteArrayToFile(croppedImageFile, decodedImageData);
//
//
//            productImage.setImageName("/productImages/" + croppedImageFileName);
//            storedProductDto.addProductImage(productImage);
//
//            // Set the Product entity into the ProductImages entity
////            productsImageRepository.save(productImage);
//        }
//    }


    @PostMapping("/addProduct2")
    public String addProducts2(@ModelAttribute("product2") ProductDto productDto,
                               HttpSession session,
                               @RequestParam("croppedImage1") MultipartFile croppedImage1,
                               @RequestParam("croppedImage2") MultipartFile croppedImage2,
                               @RequestParam("croppedImage3") MultipartFile croppedImage3,
                               @RequestParam("croppedImage4") MultipartFile croppedImage4,
                               Model model) throws IOException {

        ProductDto storedProductDto = (ProductDto) session.getAttribute("productDto");
        if (storedProductDto == null) {
            model.addAttribute("error", "please start from the beginning step");
            return "redirect:/admin/addProducts";
        }

        List<ProductImages> productImages = new ArrayList<>();

        handleCroppedImage(croppedImage1, storedProductDto, "1", productImages);
        handleCroppedImage(croppedImage2, storedProductDto, "2", productImages);
        handleCroppedImage(croppedImage3, storedProductDto, "3", productImages);
        handleCroppedImage(croppedImage4, storedProductDto, "4", productImages);

        storedProductDto.setQuantity(productDto.getQuantity());
        storedProductDto.setSubcategories(productDto.getSubcategories());
        storedProductDto.setPrice(productDto.getPrice());
        storedProductDto.setImages(productImages);

        System.out.println(productDto.getCategories()+"dhhhhhhhhhhhhhhhhh");

        Product productEntity = productService.save(storedProductDto);

        // Set the product entity for each product image
        for (ProductImages productImage : productImages) {
            productImage.setProduct(productEntity);
        }

        // Save the product images
        productsImageRepository.saveAll(productImages);

        return "redirect:/admin/addProducts";
    }

    private void handleCroppedImage(MultipartFile croppedImageData, ProductDto storedProductDto, String suffix,
                                    List<ProductImages> productImages) throws IOException {
        if (croppedImageData != null && !croppedImageData.isEmpty()) {
            byte[] decodedImageData = croppedImageData.getBytes();
            BufferedImage croppedImage = ImageIO.read(new ByteArrayInputStream(decodedImageData));
            String croppedImageFileName = storedProductDto.getProductName().trim() + "_" + suffix + ".jpg";
            File croppedImageFile = new File(UPLOAD_DIR, croppedImageFileName);
            FileUtils.writeByteArrayToFile(croppedImageFile, decodedImageData);

            ProductImages productImage = new ProductImages();
            productImage.setImageName("/productImages/" + croppedImageFileName);

            // Add product image to the list in the Product entity
            productImages.add(productImage);
        }
    }


//    ===========================  /add product using cropper.js  ============================


    @GetMapping("/updateProducts")
    public String updateProducts() {
        return "editProduct";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable(value = "id") Integer productId, Model model) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            model.addAttribute("product", product);
            model.addAttribute("productId", productId);
            return "editProduct";
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

//    @PostMapping("/editProduct/{id}")
//    public String editProduct(@PathVariable("id") Integer productId ,@ModelAttribute("product") ProductDto productDto,
//                               @RequestParam("image") MultipartFile file,Model model) throws IOException{
//
//        String croppedImageFileName = productDto.getImageName();
//        try {
//            // Validate uploaded file using Apache Tika
//            Tika tika = new Tika();
//            String mimeType = tika.detect(file.getInputStream());
//            if (!mimeType.startsWith("image")) {
//                model.addAttribute("error", "Invalid file format. Please upload an image.");
//                return "redirect:/admin/listProducts";
//            }
//
//            // Read the uploaded image into a BufferedImage
//            BufferedImage originalImage = ImageIO.read(file.getInputStream());
//             croppedImageFileName = generateUniqueFileName(Objects.requireNonNull(file.getOriginalFilename()));
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
//            // You can generate a unique filename if needed
//            File croppedImageFile = new File(UPLOAD_DIR, croppedImageFileName);
//            ImageIO.write(croppedImage, "jpg", croppedImageFile);
//            croppedImageFileName= "/productImages/" +croppedImageFileName;
//            // Set the cropped image file path in your ProductDto or store it in the database as needed
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Handle exceptions
//        }
//        productService.editProductByID(productId,productDto,croppedImageFileName );
//        return "redirect:/admin/listProducts";
//    }



    @PostMapping("/editProduct/{id}")
    @Transactional
    public String editProduct(@PathVariable("id") Integer productId, @ModelAttribute("product") ProductDto productDto,
                              @RequestParam("croppedImage1") MultipartFile image1,
                              @RequestParam("croppedImage2") MultipartFile image2,
                              @RequestParam("croppedImage3") MultipartFile image3,
                              @RequestParam("croppedImage4") MultipartFile image4) throws IOException {
        // Retrieve the existing product from the database
        Product existingProduct = productService.findByProductId(productId);

//        // Update product details from the form
//        existingProduct.setProductName(productDto.getProductName());
//        existingProduct.setPrice(productDto.getPrice());
//        existingProduct.setQuantity(productDto.getQuantity());
//        existingProduct.setDescription(productDto.getDescription());


        // Update specific product images if new files are provided
        updateProductImage(existingProduct, 0, image1);
        updateProductImage(existingProduct, 1, image2);
        updateProductImage(existingProduct, 2, image3);
        updateProductImage(existingProduct, 3, image4);

        // Save the updated product to the database
        productService.editProductByID(productId,productDto);

        return "redirect:/admin/listProducts"; // Redirect to the product list page
    }

    private void updateProductImage(Product product, int imageIndex, MultipartFile newImage) throws IOException {
        // Handle image update logic for a specific image
        // You may want to check if a new image is provided before updating
        if (newImage != null && !newImage.isEmpty()) {
            // Your logic to save and update the image URL
                byte[] decodedImageData = newImage.getBytes();
                BufferedImage croppedImage = ImageIO.read(new ByteArrayInputStream(decodedImageData));
                int index = imageIndex+1;
                String croppedImageFileName = product.getProductName()+UUID.randomUUID().toString() + "_" + index + ".jpg";
                File croppedImageFile = new File(UPLOAD_DIR, croppedImageFileName);
                FileUtils.writeByteArrayToFile(croppedImageFile, decodedImageData);

                //productImage.setImageName("/productImages/" + croppedImageFileName);

                // Add product image to the list in the Product entity
                product.getProductImages().get(imageIndex).setImageName("/productImages/"+croppedImageFileName);
                productRepository.save(product);
        }
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
