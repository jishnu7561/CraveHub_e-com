package com.project.cravehub.repository;

import com.project.cravehub.model.admin.CategoryOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryOfferRepository extends JpaRepository<CategoryOffer,Integer> {
}
