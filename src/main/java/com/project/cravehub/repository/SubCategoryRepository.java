package com.project.cravehub.repository;

import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.SubCategory;
import com.project.cravehub.service.categoryservice.SubCategoryService;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,Integer> {

    boolean existsBySubCategoryNameAndCategory(String subCategoryName, Category category);

    List<SubCategory> findAllByCategory(Category lockCategory);
}
