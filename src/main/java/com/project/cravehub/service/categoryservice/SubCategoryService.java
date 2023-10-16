package com.project.cravehub.service.categoryservice;

import com.project.cravehub.dto.CategoryDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.SubCategory;

import java.util.List;

public interface SubCategoryService {


    SubCategory save(CategoryDto categoryDto);

    public boolean isSubCatogeryExistInCategory(String subcategory ,Category category);

    public SubCategory deleteSubCategoryById(Integer id);
}
