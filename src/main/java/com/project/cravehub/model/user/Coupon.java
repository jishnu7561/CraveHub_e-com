package com.project.cravehub.model.user;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponId;

    private String couponCode;

    private String description;

    @DateTimeFormat(pattern = "MM/dd/yyy")
    private LocalDate expiryDate;

    private Double amount;

    private Double minimumPurchaseAmount;

    private boolean isActive= true;

    private Integer usageCount= 0;

    @OneToMany(mappedBy="coupon")
    private List<PurchaseOrder> ordersList;

    @ManyToMany(mappedBy = "coupons" ,  cascade = CascadeType.ALL)
    private Set<User> user = new HashSet<>();

    public Coupon() {
    }

    public void addUser(User user) {
        this.user.add(user);
        user.getCoupons().add(this);
    }

    public Coupon(String couponCode, String description, LocalDate expiryDate, Double amount, Double minimumPurchaseAmount) {
        this.couponCode = couponCode;
        this.description = description;
        this.expiryDate = expiryDate;
        this.amount = amount;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
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

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }


    public Double getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(Double minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }
}
