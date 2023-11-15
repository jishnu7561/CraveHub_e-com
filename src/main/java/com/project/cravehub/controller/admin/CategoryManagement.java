package com.project.cravehub.controller.admin;

import com.project.cravehub.dto.CategoryDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.SubCategory;
import com.project.cravehub.repository.CategoryRepository;
import com.project.cravehub.repository.SubCategoryRepository;
import com.project.cravehub.service.categoryservice.CategoryService;
import com.project.cravehub.service.categoryservice.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class CategoryManagement {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private SubCategoryRepository subCategoryRepository;


    @ModelAttribute("category")
    public CategoryDto categoryDto()
    {
        return new CategoryDto();
    }

    @ModelAttribute("subCategory")
    public CategoryDto subCategory()
    {
        return new CategoryDto();
    }

    @GetMapping("/listCategory")
    public String listCategory(Model model)
    {
        List<Category> category = categoryRepository.findAll();
        model.addAttribute("categories", category);
        List<SubCategory> subCategory = subCategoryRepository.findAll();
        model.addAttribute("subCategories",subCategory);

        return "category-management";
    }

//    @GetMapping("/listCategory")
//    public String listSubCategory(Model model)
//    {
//        List<SubCategory> subCategory = subCategoryRepository.findAll();
//        model.addAttribute("subCategories",subCategory);
//        return "redirect:/admin/listCategory";
//    }


    @PostMapping ("/listCategory")
    public String addCategory(@ModelAttribute("category") CategoryDto categoryDto)
    {
        //boolean categoryExist = categoryService.isCategoryExist(categoryDto.getCategory());
        Category categoryExist = categoryRepository.findByCategory(categoryDto.getCategory());
        if(categoryExist==null) {
            categoryService.save(categoryDto);
            return "redirect:/admin/listCategory";
        }
        return "redirect:/admin/listCategory?categoryExist";
    }


//    @GetMapping("/deleteCategory/{id}")
//    public String deleteCategory(@PathVariable(value = "id") Integer id)
//    {
//        Category deleteCategory = categoryService.deleteCategoryById(id);
//        if(deleteCategory!=null) {
//            return "redirect:/admin/listCategory";
//        }
//        return "redirect:/admin/listCategory?notExist";
//    }


    @GetMapping("/editCategory/{id}")
    public String editCategory(@PathVariable(value = "id") Integer CategoryId, Model model) {
        Optional<Category> optionalCategory = categoryRepository.findById(CategoryId);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            model.addAttribute("category", category);
            model.addAttribute("categoryId", CategoryId);
            return "edit-category";
        } else {
            return "redirect:/admin/listCategory?error";
        }
    }


    @PostMapping("/editCategory/{id}")
    public String editCategory(@PathVariable("id") Integer categoryId ,@ModelAttribute("category") CategoryDto categoryDto)
    {
        categoryService.editCategoryByID(categoryId,categoryDto);
        return "redirect:/admin/listCategory";
    }


//    @GetMapping("/listCategory")
//    public String listSubCategory(Model model) {
//        List<SubCategory> subCategory = subCategoryRepository.findAll();
//        model.addAttribute("subCategories",subCategory);
//        return "redirect:/admin/listCategory";
//    }


    @PostMapping("/addSubCategory")
    public String addSubCategory(@ModelAttribute("subCategory") CategoryDto subCategory) {
        System.out.println(subCategory.getCategory() + "-------------" + subCategory.getSubCategoryName());
        Category category = categoryRepository.findByCategory(subCategory.getCategory());
        if (category != null) {
            String subCategoryName = subCategory.getSubCategoryName().trim(); // Trim spaces
            boolean subCategoryExists = subCategoryService.isSubCategoryExistInCategory(subCategoryName, category);

            if (!subCategoryExists) {
                SubCategory newSubCategory = subCategoryService.save(subCategory);
                if (newSubCategory != null) {
                    return "redirect:/admin/listCategory";
                }
            }
            return "redirect:/admin/listCategory?subAlreadyExist";
        }
        return "redirect:/admin/listCategory?notExist";
    }


//    @GetMapping("/deleteSubCategory/{subCategoryId}")
//    public String deleteSubCategory(@PathVariable(value = "subCategoryId") Integer subCategoryId)
//    {
//        SubCategory deleteSubCategory = subCategoryService.deleteSubCategoryById(subCategoryId);
//        if(deleteSubCategory!=null) {
//            return "redirect:/admin/listCategory";
//        }
//        return "redirect:/admin/listCategory?notExist";
//    }

    @GetMapping("/blockCategory/{id}")
    public String blockCategory(@PathVariable("id") Integer id) {
        categoryService.blockCategoryById(id);
        return "redirect:/admin/listCategory";
    }

    @GetMapping("/unblockCategory/{id}")
    public String unBlockCategory(@PathVariable("id") Integer id) {
        categoryService.unBlockCategoryById(id);
        return "redirect:/admin/listCategory";
    }

    @GetMapping("/blockSubCategory/{subCategoryId}")
    public String blockSubCategory(@PathVariable("subCategoryId") Integer id) {
        subCategoryService.blockSubCategoryById(id);

        return "redirect:/admin/listCategory";
    }

    @GetMapping("/unblockSubCategory/{subCategoryId}")
    public String unblockSubCategory(@PathVariable("subCategoryId") Integer id) {
        subCategoryService.unBlockSubCategoryById(id);
        return "redirect:/admin/listCategory";
    }


}
