package com.project.cravehub.repository;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.Cart;
import com.project.cravehub.model.user.CartItem;
import com.project.cravehub.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    List<CartItem> findByCartUser(User user);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    List<CartItem> findByCart(Cart cart);

    void deleteByCart(Cart cart);

    CartItem findByProduct(Product product);
}
