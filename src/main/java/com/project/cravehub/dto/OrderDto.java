package com.project.cravehub.dto;

import com.project.cravehub.model.user.Address;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.model.user.User;

import java.time.LocalDate;
import java.util.List;

public class OrderDto {

    private Integer orderId;

    private LocalDate orderedDate;

    private Double orderAmount;

    private String paymentMethodName;

    private String orderStatus;

    private String[] orderStatusList = {"Requested for Return","Shipped","Pending","Delivered","Cancelled","Refunded","Returned"};

   // private Integer addressId;

    private Address addressId;

    private User userId;

    private List<OrderItem> orderItems;

    public OrderDto() {
        super();
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(LocalDate orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String[] getOrderStatusList() {
        return orderStatusList;
    }

    private String paymentStatus;

    public void setOrderStatusList(String[] orderStatusList) {
        this.orderStatusList = orderStatusList;
    }

//    public Integer getAddressId() {
//        return addressId;
//    }
//
//    public void setAddressId(Integer addressId) {
//        this.addressId = addressId;
//    }


    public Address getAddressId() {
        return addressId;
    }

    public void setAddressId(Address addressId) {
        this.addressId = addressId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
