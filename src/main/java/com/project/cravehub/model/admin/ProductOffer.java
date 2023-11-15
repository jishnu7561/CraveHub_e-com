package com.project.cravehub.model.admin;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ProductOffer {

        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        private Integer productOfferId;

        private String productOfferName;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate expiryDate;

        private Double discountPercentage;

        private boolean isEnabled = true;

        private boolean isActive = true;

        @OneToMany(mappedBy = "productOffer",cascade={CascadeType.PERSIST, CascadeType.MERGE},fetch=FetchType.EAGER)
        private Set<Product> productList = new HashSet<>();

    public ProductOffer() {
    }

    public ProductOffer(String productOfferName, LocalDate startDate, LocalDate expiryDate, Double discountPercentage) {
        this.productOfferName = productOfferName;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.discountPercentage = discountPercentage;
    }

    public Integer getProductOfferId() {
        return productOfferId;
    }

    public void setProductOfferId(Integer productOfferId) {
        this.productOfferId = productOfferId;
    }

    public String getProductOfferName() {
        return productOfferName;
    }

    public void setProductOfferName(String productOfferCode) {
        this.productOfferName = productOfferCode;
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

    @PrePersist
    @PreUpdate
    public void checkOfferValidity() {
        LocalDate currentDate = LocalDate.now();

        if (isActive) {
            boolean isBeforeExpiry = expiryDate == null || currentDate.isBefore(expiryDate);
            boolean isAfterOrEqualStartDate = startDate == null || !currentDate.isBefore(startDate);

            if (isBeforeExpiry && isAfterOrEqualStartDate) {
                // If the offer is active, the current date is before the expiry date,
                // and the current date is after or equal to the start date (if start date is not null)
                isEnabled = true;
            } else {
                // If the expiry date is before the current date, or the current date is before the start date (if start date is not null)
                isEnabled = false;
            }

        } else {
            // If the offer is not active
            isEnabled = false;
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
