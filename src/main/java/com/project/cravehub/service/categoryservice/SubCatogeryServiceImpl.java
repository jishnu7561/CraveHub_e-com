package com.project.cravehub.service.categoryservice;

import com.project.cravehub.dto.CategoryDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.admin.SubCategory;
import com.project.cravehub.repository.CategoryRepository;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCatogeryServiceImpl implements SubCategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public SubCategory save(CategoryDto categoryDto) {

        Category category = categoryRepository.findByCategory(categoryDto.getCategory());

        if(category!=null)
        {
            SubCategory subCategory = new SubCategory(categoryDto.getSubCategoryName(),category);
            return subCategoryRepository.save(subCategory);
        }
        return null;
    }

    @Override
    public boolean isSubCategoryExistInCategory(String subcategory ,Category category) {


      return subCategoryRepository.existsBySubCategoryNameAndCategory(subcategory, category);

    }

//    public SubCategory deleteSubCategoryById(Integer id) {
//        Optional<SubCategory> optionalSubCategory = subCategoryRepository.findById(id);
//        if(optionalSubCategory.isPresent()) {
//            subCategoryRepository.deleteById(id);
//            return optionalSubCategory.get();
//        }else {
//            return null;
//        }
//        //return Optional.empty();
////        return null;
//    }

    @Override
    public void blockSubCategoryById(Integer id) {
        SubCategory lockSubCategory = subCategoryRepository.findById(id).orElse(null);
        if(lockSubCategory != null) {
            lockSubCategory.setEnabled(true);
            subCategoryRepository.save(lockSubCategory);

            // Block products under the category
            List<Product> productsUnderCategory = productRepository.findAllBySubcategories(lockSubCategory);
            for (Product product : productsUnderCategory) {
                product.setEnabled(true);
                productRepository.save(product);
            }
        }
    }

    @Override
    public void unBlockSubCategoryById(Integer id) {
        SubCategory unLockSubCategory = subCategoryRepository.findById(id).orElse(null);
        if(unLockSubCategory != null) {
            unLockSubCategory.setEnabled(false);
            subCategoryRepository.save(unLockSubCategory);

            List<Product> productsUnderCategory = productRepository.findAllBySubcategories(unLockSubCategory);
            for (Product product : productsUnderCategory) {
                product.setEnabled(false);
                productRepository.save(product);
            }
        }
    }

}

