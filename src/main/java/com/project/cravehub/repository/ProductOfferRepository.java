package com.project.cravehub.repository;

import com.project.cravehub.model.admin.ProductOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOfferRepository extends JpaRepository<ProductOffer,Integer> {
}
