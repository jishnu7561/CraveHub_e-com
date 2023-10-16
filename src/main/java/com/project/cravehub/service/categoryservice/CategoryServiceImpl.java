package com.project.cravehub.service.categoryservice;

import com.project.cravehub.dto.CategoryDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public void blockCategoryById(Integer id) {
        Category lockCategory = categoryRepository.findById(id).get();
        lockCategory.setEnabled(true);
        categoryRepository.save(lockCategory);
    }

    @Override
    public void unBlockCategoryById(Integer id) {
        Category unLockCategory = categoryRepository.findById(id).get();
        unLockCategory.setEnabled(false);
        categoryRepository.save(unLockCategory);
    }

    @Override
    public Category save(CategoryDto categoryDto) {
        Category category = new Category(categoryDto.getCategory());
        return  categoryRepository.save(category);
    }

    @Override
    public Category deleteCategoryById(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(optionalCategory.isPresent()) {
            categoryRepository.deleteById(id);
            return optionalCategory.get();
        }else {
            return null;
        }
        //return Optional.empty();
//        return null;
    }

    @Override
    @Transactional
    public void editCategoryByID(Integer categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if(category!=null)
        {
            category.setCategory(categoryDto.getCategory());
            categoryRepository.save(category);
        }
//        categoryRepository.save(category);
    }


}
