package com.project.cravehub.dto;

public class ResponseDto {
    private int updatedQuantity;
    private double totalPrice;

    public ResponseDto() {
    }

    public ResponseDto(int updatedQuantity, double totalPrice) {
        this.updatedQuantity = updatedQuantity;
        this.totalPrice = totalPrice;
    }

    public int getUpdatedQuantity() {
        return updatedQuantity;
    }

    public void setUpdatedQuantity(int updatedQuantity) {
        this.updatedQuantity = updatedQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
