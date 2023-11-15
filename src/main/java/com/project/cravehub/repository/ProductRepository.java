package com.project.cravehub.repository;

import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.admin.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findAllByCategories(Category category);

    List<Product> findAllBySubcategories(SubCategory subCategory);

    Page<Product> findAllByIsEnabledFalse(Pageable pageable);

    Page<Product> findAllByCategories_CategoryAndIsEnabledFalse(String category, Pageable pageable);

//    Page<Product> findByProductNameContainingOrCategoriesContaining(String searchTerm, String searchTerm1, Pageable pageable);

    Page<Product> findByProductNameContainingOrCategoriesCategoryContainingOrSubcategoriesSubCategoryNameContaining(String searchTerm1, String searchTerm2, String searchTerm3, Pageable pageable);

    List<Product> findByProductNameContaining(String searchTerm);
}
