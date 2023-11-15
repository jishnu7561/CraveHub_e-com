package com.project.cravehub.dto;

import com.project.cravehub.model.admin.Product;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class OfferDto {

    private Integer productOfferId;
    private String productOfferName;
    private String startDate;
    private String expiryDate;

    private String categoryOfferName;

    private Double discountPercentage;

//        private boolean isEnabled=true;
    private boolean isActive ;
    private Set<Product> productList = new HashSet<>();


    public Integer getProductOfferId() {
        return productOfferId;
    }

    public void setProductOfferId(Integer productOfferId) {
        this.productOfferId = productOfferId;
    }

    public String getProductOfferName() {
        return productOfferName;
    }

    public void setProductOfferName(String productOfferName) {
        this.productOfferName = productOfferName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<Product> getProductList() {
        return productList;
    }

    public void setProductList(Set<Product> productList) {
        this.productList = productList;
    }

    public String getCategoryOfferName() {
        return categoryOfferName;
    }

    public void setCategoryOfferName(String categoryOfferName) {
        this.categoryOfferName = categoryOfferName;
    }
}
