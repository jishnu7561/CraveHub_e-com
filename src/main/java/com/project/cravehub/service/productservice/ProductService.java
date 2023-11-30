package com.project.cravehub.service.productservice;

import com.project.cravehub.dto.OfferDto;
import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.admin.ProductOffer;
import com.project.cravehub.model.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product save(ProductDto productDto);

    //Product deleteProductById(Integer productId);

    //public Product deleteProduct(Integer id);

//    Product deleteProductById(Integer productId);

    Product getProductById(Integer productId);

    void editProductByID(Integer productId, ProductDto productDto);

    boolean isExist(Integer productId);

    void unBlockProductById(Integer id);

    void blockProductById(Integer id);

    List<Product> findAllProducts();

//    void deleteProductB(Integer id);
    Page<Product>  findAllProducts(int page ,int pageSize);

    Page<Product> findProductsByCategory(String category, int page, int pageSize);

    Page<Product> findProductsBySearchTerm(String searchTerm, int page, int pageSize);

    ProductOffer saveProductOffer(OfferDto offerDto);

    void inactiveProductOffer(Integer id);

    void activeProductOffer(Integer id);

    boolean editProductOfferById(Integer id,OfferDto offerDto);

    List<Product> getProductsByPartialProductName(String searchTerm);

    String addProductToOffer(Integer productId, Integer offerId);

    boolean removeProductFromOffer(Integer offerId, Integer productId);

    Integer totalSold(Product product,User user);

    void updateIsEnabled();

    boolean findProductNameExist(String productName);

    Product findByProductId(Integer id);
}
