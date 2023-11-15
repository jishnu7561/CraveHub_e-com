package com.project.cravehub.repository;

import com.project.cravehub.model.user.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Integer> {
    boolean findByUser(Integer wishlistId);
}
