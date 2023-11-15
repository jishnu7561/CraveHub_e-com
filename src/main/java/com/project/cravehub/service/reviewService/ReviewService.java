package com.project.cravehub.service.reviewService;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.OrderItem;

import java.security.Principal;

public interface ReviewService {
    boolean addReview(Integer productId, Principal principal,Integer rating,String comment,OrderItem orderItem);

    boolean existsByUserAndOrderItemAndProduct(Principal principal, OrderItem orderItem);

    Integer getReviewCountForProduct(Product product);
}
