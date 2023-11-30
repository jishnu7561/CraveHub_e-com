package com.project.cravehub.repository;

import com.project.cravehub.model.admin.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface ProductsImageRepository extends JpaRepository<ProductImages,Integer> {
}
