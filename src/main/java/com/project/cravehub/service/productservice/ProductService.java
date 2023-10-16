package com.project.cravehub.service.productservice;

import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.admin.Product;

import java.util.Optional;

public interface ProductService {

    public void save(ProductDto productDto);

    //Product deleteProductById(Integer productId);

    //public Product deleteProduct(Integer id);

    Product deleteProductById(Integer productId);

    Product getProductById(Integer productId);

    void editProductByID(Integer productId, ProductDto productDto);

//    void deleteProductB(Integer id);
}
