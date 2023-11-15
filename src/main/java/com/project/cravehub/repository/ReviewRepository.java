package com.project.cravehub.repository;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.model.user.Review;
import com.project.cravehub.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {

    boolean existsByUserAndOrderItemAndProduct(User user, OrderItem orderItem, Product product);

    Integer countByProduct(Product product);
}
