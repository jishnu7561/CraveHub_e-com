package com.project.cravehub.service.categoryservice;

import com.project.cravehub.dto.CategoryDto;
import com.project.cravehub.dto.OfferDto;
import com.project.cravehub.model.admin.*;
import com.project.cravehub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryOfferRepository categoryOfferRepository;

    @Autowired
    private ProductOfferRepository productOfferRepository;


    @Override
    public void blockCategoryById(Integer id) {
        Category lockCategory = categoryRepository.findById(id).orElse(null);
        if (lockCategory != null) {
            // Block the category
            lockCategory.setEnabled(true);
            categoryRepository.save(lockCategory);

            // Block products under the category
            List<Product> productsUnderCategory = productRepository.findAllByCategories(lockCategory);
            for (Product product : productsUnderCategory) {
                product.setEnabled(true);
                productRepository.save(product);
            }

            // Block subcategories under the category
            List<SubCategory> subcategoriesUnderCategory = subCategoryRepository.findAllByCategory(lockCategory);
            for (SubCategory subcategory : subcategoriesUnderCategory) {
                subcategory.setEnabled(true);
                subCategoryRepository.save(subcategory);
            }
        }
    }


    @Override
    public void unBlockCategoryById(Integer id) {
        Category unLockCategory = categoryRepository.findById(id).get();
        unLockCategory.setEnabled(false);
        categoryRepository.save(unLockCategory);

        List<Product> productsUnderCategory = productRepository.findAllByCategories(unLockCategory);
        for (Product product : productsUnderCategory) {
            product.setEnabled(false);
            productRepository.save(product);
        }

        List<SubCategory> subcategoriesUnderCategory = subCategoryRepository.findAllByCategory(unLockCategory);
        for (SubCategory subcategory : subcategoriesUnderCategory) {
            subcategory.setEnabled(false);
            subCategoryRepository.save(subcategory);
        }

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
    }


    @Override
    public CategoryOffer saveCategoryOffer(OfferDto offerDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(offerDto.getStartDate(), formatter);
        LocalDate expiryDate = LocalDate.parse(offerDto.getExpiryDate(), formatter);
        CategoryOffer categoryOffer = new CategoryOffer(offerDto.getCategoryOfferName(),
                startDate,expiryDate,offerDto.getDiscountPercentage());
        return categoryOfferRepository.save(categoryOffer);
    }

    @Override
    public void inactiveCategoryOffer(Integer id) {
        CategoryOffer inactivateCategoryOffer = categoryOfferRepository.findById(id).get();
        inactivateCategoryOffer.setActive(false);
        categoryOfferRepository.save(inactivateCategoryOffer);
    }

    @Override
    public void activeCategoryOffer(Integer id) {
        CategoryOffer activateCategoryOffer = categoryOfferRepository.findById(id).get();
        activateCategoryOffer.setActive(true);
        categoryOfferRepository.save(activateCategoryOffer);
    }

    @Override
    public boolean editCategoryOfferById(Integer id,OfferDto offerDto) {
        Optional<CategoryOffer> categoryOfferOptional = categoryOfferRepository.findById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(offerDto.getStartDate(), formatter);
        LocalDate expiryDate = LocalDate.parse(offerDto.getExpiryDate(), formatter);
        if(categoryOfferOptional.isPresent())
        {
            CategoryOffer categoryOffer = categoryOfferOptional.get();
            categoryOffer.setCategoryOfferName(offerDto.getCategoryOfferName());
            categoryOffer.setDiscountPercentage(offerDto.getDiscountPercentage());
            categoryOffer.setStartDate(startDate);
            categoryOffer.setExpiryDate(expiryDate);
            categoryOfferRepository.save(categoryOffer);
            return true;
        }
        return false;
    }

    @Override
    public List<Category> getCategoriesByPartialCategory(String searchTerm) {
        return categoryRepository.findByCategoryContaining(searchTerm);
    }

    @Transactional
    @Override
    public String addCategoryToOffer(Integer categoryId, Integer offerId) {
        System.out.println("addCategoryToOffer called successfully");
        Optional<CategoryOffer> categoryOfferOptional = categoryOfferRepository.findById(offerId);
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(categoryOfferOptional.isPresent() && categoryOptional.isPresent())
        {
            Category category = categoryOptional.get();
            CategoryOffer categoryOffer = categoryOfferOptional.get();
            Set<Product> productSet = category.getProducts();
            for(Product product:productSet)
            {
                System.out.println(product.getProductName());
            }
            if(categoryOffer.getCategories().contains(category))
            {
                return "exist";
            }
            for(Product product : productSet) {
                if(product.getProductOffer() != null)
                {
                    Optional<ProductOffer> productOffers = productOfferRepository.findById(product.getProductOffer().getProductOfferId());
                    if(productOffers.isPresent())
                    {
                        productOffers.get().getProductList().remove(product);
                        productOfferRepository.save(productOffers.get());
                    }
                    product.setDiscountedPrice(0.0);
                    product.setProductOffer(null);
                }
                double discountAmount = (product.getPrice() * categoryOffer.getDiscountPercentage()) / 100;
                double discountedPrice = product.getPrice() - discountAmount;
                product.setDiscountedPrice(discountedPrice);
                System.out.println("products category "+ discountedPrice);
                productRepository.save(product);
            }
                categoryOffer.getCategories().add(category);
                category.setCategoryOffer(categoryOffer);
//                categoryRepository.save(category);
                categoryOfferRepository.save(categoryOffer);
            return "success";
        }
        return "failed";
    }


    @Override
    public boolean removeCategoryFromOffer(Integer offerId, Integer productId) {
        Optional<CategoryOffer> categoryOfferOptional = categoryOfferRepository.findById(offerId);
        Optional<Category> categoryOptional = categoryRepository.findById(productId);
        if(categoryOfferOptional.isPresent() && categoryOptional.isPresent())
        {
            CategoryOffer categoryOffer = categoryOfferOptional.get();
            Category category = categoryOptional.get();
            Set<Product> productSet = category.getProducts();
            for(Product product : productSet) {
                product.setDiscountedPrice(0.0);
                productRepository.save(product);
            }
            categoryOffer.getCategories().remove(category);
            categoryOfferRepository.save(categoryOffer);
            category.setCategoryOffer(null);
            categoryRepository.save(category);
            return true;
        }
        return false;
    }

    public void updateIsEnabled() {
        List<CategoryOffer> categoryOfferList = categoryOfferRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for(CategoryOffer categoryOffer : categoryOfferList) {
                boolean isBeforeOrEqualExpiryDate = currentDate.isBefore(categoryOffer.getExpiryDate()) || currentDate.isEqual(categoryOffer.getExpiryDate());
                boolean isAfterOrEqualStartDate = currentDate.isAfter(categoryOffer.getStartDate()) || currentDate.isEqual(categoryOffer.getStartDate());
                System.out.println("isBeforeOrEqualExpiry"+ isBeforeOrEqualExpiryDate);
                System.out.println("isAfterOrEqualStartDate"+ isAfterOrEqualStartDate);
                if (categoryOffer.isActive() && isBeforeOrEqualExpiryDate && isAfterOrEqualStartDate) {
                    System.out.println(categoryOffer.getCategories());
                    categoryOffer.setEnabled(true);
                } else {
                    categoryOffer.setEnabled(false);
                }
                try {
                    categoryOfferRepository.save(categoryOffer);
                } catch (Exception e) {
                    e.printStackTrace(); // Log the exception
                }
        }
    }


}
