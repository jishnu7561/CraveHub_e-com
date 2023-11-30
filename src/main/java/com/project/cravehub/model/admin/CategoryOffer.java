package com.project.cravehub.model.admin;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CategoryOffer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer categoryOfferId;

    private String categoryOfferName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private boolean isEnabled =true;

    private boolean isActive=true;

    private Double discountPercentage;

    @OneToMany(mappedBy = "categoryOffer",cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.EAGER)
    private Set<Category> categories = new HashSet<>();

    public CategoryOffer() {
    }

    public CategoryOffer(String productOfferName, LocalDate startDate, LocalDate expiryDate, Double discountPercentage) {
        this.categoryOfferName = productOfferName;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.discountPercentage = discountPercentage;
    }

    public Integer getCategoryOfferId() {
        return categoryOfferId;
    }

    public void setCategoryOfferId(Integer categoryOfferId) {
        this.categoryOfferId = categoryOfferId;
    }

    public String getCategoryOfferName() {
        return categoryOfferName;
    }

    public void setCategoryOfferName(String categoryOfferName) {
        this.categoryOfferName = categoryOfferName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
