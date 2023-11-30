package com.project.cravehub.service.categoryservice;

import com.project.cravehub.dto.CategoryDto;
import com.project.cravehub.dto.OfferDto;
import com.project.cravehub.model.admin.*;

import java.util.List;

public interface CategoryService {

     public void blockCategoryById(Integer id);

     public void unBlockCategoryById(Integer id);


    Category save(CategoryDto category);

    Category deleteCategoryById(Integer id);

    void editCategoryByID(Integer categoryId, CategoryDto categoryDto);

    CategoryOffer saveCategoryOffer(OfferDto offerDto);

    void inactiveCategoryOffer(Integer id);

    void activeCategoryOffer(Integer id);

    boolean editCategoryOfferById(Integer id,OfferDto offerDto);

    List<Category> getCategoriesByPartialCategory(String searchTerm);

    String addCategoryToOffer(Integer categoryId, Integer offerId);

    boolean removeCategoryFromOffer(Integer offerId, Integer categoryId);

    void updateIsEnabled();
}
