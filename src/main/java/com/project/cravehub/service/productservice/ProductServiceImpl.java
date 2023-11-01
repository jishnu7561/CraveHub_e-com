package com.project.cravehub.service.productservice;

import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.admin.Category;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.CartItem;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public void save(ProductDto productDto) {
        Product products = new Product(productDto.getProductName(),productDto.getDescription(),
                productDto.getCategories(), productDto.getImageName(), productDto.getPrice(),productDto.getQuantity(),productDto.getSubcategories());
        productRepository.save(products);
    }

//    @Override
//    public Product deleteProduct(Integer id) {
//        Optional<Product> optionalProduct = productRepository.findById(id);
//
//        if (optionalProduct.isPresent()) {
//            Product deleteProduct = optionalProduct.get();
//            Set<SubCategory> subCategories = (Set<SubCategory>) deleteProduct.getSubcategories();
//
//            // Check if subCategories is not null before iterating over it
//            if (subCategories != null) {
//                Category c;
//                for(SubCategory s : subCategories) {
//                    s.getProducts().remove(deleteProduct);
//                    c = s.getCategory();
//                    c.getProducts().remove(deleteProduct);
//                    categoryRepository.save(c);
//                    subCategoryRepository.save(s);
//                }
//            }
//
//            productRepository.deleteById(id);
//        }
//        return  null;
//    }


//    @Override
//    public void deleteProduct(Integer id) {
//
//    }

//    public void deleteProduct(Integer id) {
//
//        Product deleteProduct = productRepository.findById(id).get();
//        Set<SubCategory> subCategories = (Set<SubCategory>) deleteProduct.getSubcategories();
//        Category c;
//        for(SubCategory s:subCategories) {
//            s.getProducts().remove(deleteProduct);
//            c = s.getCategory();
//            c.getProducts().remove(deleteProduct);
//            categoryRepository.save(c);
//            subCategoryRepository.save(s);
//        }
//        productRepository.deleteById(id);
//    }





    @Override
    public Product deleteProductById(Integer productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()) {
            productRepository.deleteById(productId);
            return optionalProduct.get();
        }else {
            return null;
        }
    }

    @Override
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public void editProductByID(Integer productId, ProductDto productDto) {
        Product product = productRepository.findById(productId).orElse(null);
        if(product!=null)
        {
            product.setProductName(productDto.getProductName());
            product.setDescription(productDto.getDescription());
            product.setImageName(productDto.getImageName());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());
            productRepository.save(product);
        }
    }

    @Override
    public boolean isExist(Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isPresent()) {
            Product product = productOptional.get();
            CartItem cartItem = cartItemRepository.findByProduct(product);
            List<OrderItem> orderItems = orderItemRepository.findByProduct(product);
            if(cartItem != null || !orderItems.isEmpty())
            {
                return true;
            }
            return false;
        }
        return false;
    }


//    @Override
//    public void save2(ProductDto productDto2) {
//        Product products2 = new Product( productDto2.getSubcategories());
//        productRepository.save(products2);
//    }
}
