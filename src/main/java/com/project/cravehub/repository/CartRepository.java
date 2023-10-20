package com.project.cravehub.repository;

import com.project.cravehub.model.user.Cart;
import com.project.cravehub.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Cart findByUser(User user);
}
