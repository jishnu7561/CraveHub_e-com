package com.project.cravehub.service.categoryservice;

import com.project.cravehub.dto.CategoryDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.SubCategory;

import java.util.List;

public interface CategoryService {

     public void blockCategoryById(Integer id);

     public void unBlockCategoryById(Integer id);


    Category save(CategoryDto category);

    Category deleteCategoryById(Integer id);

    void editCategoryByID(Integer categoryId, CategoryDto categoryDto);

}
