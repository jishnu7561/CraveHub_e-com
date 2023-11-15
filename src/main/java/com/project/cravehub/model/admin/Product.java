package com.project.cravehub.model.admin;

import com.project.cravehub.model.user.Review;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer productId;

    @Column(name="productName",nullable=false)
    private String productName;

    @Column(name="description",nullable=false)
    private String description;


    @Column(name="price")
    private Double price;

    @Column(name="quantity")
    private Integer quantity;

    @Transient
    private String status;


//    @ManyToMany(mappedBy = "products", cascade = CascadeType.MERGE)
//    private Set<Category> categories;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categories;

//    @ManyToMany(cascade = CascadeType.MERGE)
//    @JoinTable(name = "product_subcategories",
//            joinColumns = @JoinColumn(name = "product_id"),
//            inverseJoinColumns = @JoinColumn(name = "subcategory_id"))
//    private List<SubCategory> subcategories;

    @ManyToOne
    @JoinColumn(name = "subCategoryId")
    private SubCategory subcategories;

    private String imageName;

    @Column(name="isEnabled",nullable=false)
    private boolean isEnabled;

    @ManyToOne
    @JoinColumn(name = "product_offer_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private ProductOffer productOffer;

    private Double discountedPrice=0.0;

    private double averageRating;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews;

    public Product() {
    }



    public Product(String productName, String description, Double price, Integer quantity,
                   Category categories, SubCategory subcategories,
                   String imageName, boolean isEnabled) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.categories = categories;
        this.subcategories = subcategories;
        this.imageName= imageName;
        this.isEnabled = isEnabled;
    }

    public Product(String productName, String description, Category categories, String imageName,Double price) {
        this.productName = productName;
        this.description = description;
        this.categories = categories;
        this.imageName = imageName;
        this.price = price;
    }

    public Product(SubCategory subcategories) {
//        this.price = price;
//        this.quantity = quantity;
        this.subcategories = subcategories;
    }



    public Product(String productName, String description, Category categories, String imageName, Double price, Integer quantity, SubCategory subcategories) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.categories = categories;
        this.subcategories = subcategories;
        this.imageName = imageName;
    }

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

//    public Set<Category> getCategories() {
//        return categories;
//    }
//
//    public void setCategories(Set<Category> categories) {
//        this.categories = categories;
//    }


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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getStatus() {
        if(quantity>0)
        {
            return "<span class='stock_in'>Stock In</span>";
        }
        else{
            return "<span class='stock_out'>Stock Out</span>";
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductOffer getProductOffer() {
        return productOffer;
    }

    public void setProductOffer(ProductOffer productOffer) {
        this.productOffer = productOffer;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // New method to add a review to the product
    public void addReview(Review review) {
        reviews.add(review);
        calculateAverageRating();
    }

    // New method to calculate the average rating
    public void calculateAverageRating() {
        if (!reviews.isEmpty()) {
            double totalRating = reviews.stream().mapToDouble(Review::getRating).sum();
            averageRating = totalRating / reviews.size();
            System.out.println("aaaaaaaaaaaaaa"+ averageRating);
        }
    }

}