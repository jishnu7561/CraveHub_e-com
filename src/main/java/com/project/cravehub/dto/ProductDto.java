package com.project.cravehub.dto;

import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.ProductImages;
import com.project.cravehub.model.admin.SubCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDto {

    private Integer productId;
    private String productName;
    private String description;
    private Double price;
    private Integer quantity;
    private Category categories;
    private SubCategory subcategories ;
    private List<ProductImages> productImages = new ArrayList<>();

    private String productImage ;

    private boolean isEnabled;


    public ProductDto() {
    }

    //
//    private Set<Category> category;
//    private Set<SubCategory> subcategory;
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Category getCategories() {
        return categories;
    }

    public void setCategories(Category categories) {
        this.categories = categories;
    }

    public SubCategory getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(SubCategory subcategories) {
        this.subcategories = subcategories;
    }


    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }


//    public void addImage(String imageName) {
//        this.images.add(imageName);
//    }
//
//
//    // New method to remove an image from the product
//    public void removeImage(String imageName) {
//        images.remove(imageName);
//    }


    public List<ProductImages> getImages() {
        return productImages;
    }

    public void setImages(List<ProductImages> images) {
        this.productImages = images;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void addProductImage(ProductImages productImage) {
        productImages.add(productImage);
    }

    // New method to remove a product image from the product
    public void removeProductImage(ProductImages productImage) {
        productImages.remove(productImage);
    }
}
