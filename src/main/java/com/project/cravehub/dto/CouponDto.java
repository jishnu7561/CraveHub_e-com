package com.project.cravehub.dto;

import com.project.cravehub.model.user.PurchaseOrder;
import com.project.cravehub.model.user.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CouponDto {

    private Integer couponId;

    private String couponCode;

    private String description;

    private String expiryDate;

    private Double amount;

    private Double minimumPurchaseAmount;

    private boolean isActive;
    private Integer usageCount;
    private List<PurchaseOrder> ordersList;

    private Set<User> user = new HashSet<>();



    public LocalDate getExpiryDateAsLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return LocalDate.parse(expiryDate, formatter);
    }

    public CouponDto() {
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(Double minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public List<PurchaseOrder> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<PurchaseOrder> ordersList) {
        this.ordersList = ordersList;
    }

    public Set<User> getUser() {
        return user;
    }

    public void setUser(Set<User> user) {
        this.user = user;
    }
}
