package com.project.cravehub.dto;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDto {

    private Integer wishlistId;
    private User user;
    List<Product> products = new ArrayList<>();

    public Integer getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Integer wishlistId) {
        this.wishlistId = wishlistId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
