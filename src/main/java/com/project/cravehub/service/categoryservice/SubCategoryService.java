package com.project.cravehub.service.categoryservice;

import com.project.cravehub.dto.CategoryDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.SubCategory;

import java.util.List;

public interface SubCategoryService {


    SubCategory save(CategoryDto categoryDto);

    public boolean isSubCategoryExistInCategory(String subcategory ,Category category);

    void blockSubCategoryById(Integer id);

    void unBlockSubCategoryById(Integer id);

//    public SubCategory deleteSubCategoryById(Integer id);
}
