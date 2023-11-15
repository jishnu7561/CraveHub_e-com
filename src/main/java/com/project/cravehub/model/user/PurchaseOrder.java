package com.project.cravehub.model.user;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer orderId;

    @OneToOne
    private Address address;

    @OneToMany(mappedBy = "order" ,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    private User user;

    private String paymentMethod;

    private LocalDateTime orderDate;

    private Double OrderAmount;

    private  String transactionId;

    private String paymentStatus;

    private boolean refund_used = false;
    @ManyToOne
    private Coupon coupon;

//    private String purpose;

    public PurchaseOrder(Address address, User user, String paymentMethod, LocalDateTime orderDate, Double orderAmount) {
        this.address = address;
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        OrderAmount = orderAmount;
    }

    public PurchaseOrder() {

    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Double getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        OrderAmount = orderAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

//    public Integer getOrderedQuantity() {
//        return orderedQuantity;
//    }
//
//    public void setOrderedQuantity(Integer orderedQuantity) {
//        this.orderedQuantity = orderedQuantity;
//    }


    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isRefund_used() {
        return refund_used;
    }

    public void setRefund_used(boolean refund_used) {
        this.refund_used = refund_used;
    }
}
