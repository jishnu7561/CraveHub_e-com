package com.project.cravehub.service.productservice;

import com.project.cravehub.dto.OfferDto;
import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.admin.ProductOffer;
import com.project.cravehub.model.user.*;
import com.project.cravehub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Autowired
    private ProductOfferRepository productOfferRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public Product save(ProductDto productDto) {
        Product products = new Product(productDto.getProductName(),productDto.getDescription(),
                productDto.getCategories(), productDto.getPrice(),productDto.getQuantity(),productDto.getSubcategories(),productDto.getImages());
        productRepository.save(products);
        return products;
    }

//    @Override
//    public Product deleteProductById(Integer productId) {
//        Optional<Product> optionalProduct = productRepository.findById(productId);
//        if(optionalProduct.isPresent()) {
//            productRepository.deleteById(productId);
//            return optionalProduct.get();
//        }else {
//            return null;
//        }
//    }

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

    @Override
    public void unBlockProductById(Integer id) {
        Product unLockProduct = productRepository.findById(id).orElse(null);
        if(unLockProduct != null) {
            unLockProduct.setEnabled(false);
            productRepository.save(unLockProduct);
        }

    }

    @Override
    public void blockProductById(Integer id) {
        Product lockProduct = productRepository.findById(id).orElse(null);
        if(lockProduct != null) {
            lockProduct.setEnabled(true);
            productRepository.save(lockProduct);
        }
    }

    @Override
    public List<Product> findAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        List<Product> productList = new ArrayList<>();
        for(Product product : allProducts) {
            if(!product.isEnabled() )
            {
                 productList.add(product);
            }
        }
        return productList;
    }

    @Override
    public Page<Product> findAllProducts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return productRepository.findAllByIsEnabledFalse(pageable);

    }

    @Override
    public Page<Product> findProductsByCategory(String category, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return productRepository.findAllByCategories_CategoryAndIsEnabledFalse(category,pageable);
    }

    @Override
    public Page<Product> findProductsBySearchTerm(String searchTerm, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return productRepository.findByProductNameContainingOrCategoriesCategoryContainingOrSubcategoriesSubCategoryNameContaining(searchTerm, searchTerm, searchTerm, pageable);
    }

    @Override
    public ProductOffer saveProductOffer(OfferDto offerDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(offerDto.getStartDate(), formatter);
        LocalDate expiryDate = LocalDate.parse(offerDto.getExpiryDate(), formatter);
        ProductOffer productOffer = new ProductOffer(offerDto.getProductOfferName(),
                startDate,expiryDate,offerDto.getDiscountPercentage());
        return productOfferRepository.save(productOffer);
    }

    @Override
    public void inactiveProductOffer(Integer id) {
        ProductOffer inactivateProductOffer = productOfferRepository.findById(id).get();
        inactivateProductOffer.setActive(false);
        productOfferRepository.save(inactivateProductOffer);
    }

    @Override
    public void activeProductOffer(Integer id) {
        ProductOffer activateProductOffer = productOfferRepository.findById(id).get();
        activateProductOffer.setActive(true);
        productOfferRepository.save(activateProductOffer);
    }

    @Override
    public boolean editProductOfferById(Integer id,OfferDto offerDto) {
        Optional<ProductOffer> productOfferOptional = productOfferRepository.findById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(offerDto.getStartDate(), formatter);
        LocalDate expiryDate = LocalDate.parse(offerDto.getExpiryDate(), formatter);
        if(productOfferOptional.isPresent())
        {
            ProductOffer productOffer = productOfferOptional.get();
            productOffer.setProductOfferName(offerDto.getProductOfferName());
            productOffer.setDiscountPercentage(offerDto.getDiscountPercentage());
            productOffer.setStartDate(startDate);
            productOffer.setExpiryDate(expiryDate);
            productOfferRepository.save(productOffer);
            addDiscountToProduct(productOffer);
            return true;
        }
        return false;
    }

    private void addDiscountToProduct(ProductOffer productOffer) {
        Set<Product> productSet = productOffer.getProductList();
        for (Product product : productSet) {
            double discountAmount = (product.getPrice() * productOffer.getDiscountPercentage())/100;
            double discountedPrice = product.getPrice() - discountAmount;
            product.setDiscountedPrice(discountedPrice);
            productRepository.save(product);
        }
    }

    @Override
    public List<Product> getProductsByPartialProductName(String searchTerm) {
        return productRepository.findByProductNameContaining(searchTerm);
    }

    @Transactional
    @Override
    public String addProductToOffer(Integer productId, Integer offerId) {
        Optional<ProductOffer> productOfferOptional = productOfferRepository.findById(offerId);
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOfferOptional.isPresent() && productOptional.isPresent())
        {
            Product product = productOptional.get();
            if(product.getCategories().getCategoryOffer()!= null && product.getCategories().getCategoryOffer().isEnabled())
            {
                return "categoryExist";
            }
            ProductOffer productOffer = productOfferOptional.get();
            double discountAmount = (product.getPrice() * productOffer.getDiscountPercentage())/100;
            double discountedPrice = product.getPrice() - discountAmount;
            if(productOffer.getProductList().contains(product))
            {
                return "exist";
            }
            productOffer.getProductList().add(product);
            product.setProductOffer(productOffer);
            product.setDiscountedPrice(discountedPrice);
            productOfferRepository.save(productOffer);
            product.setProductOffer(productOffer);
            productRepository.save(product);
            return "success";
        }
        return "failed";
    }


    @Override
    public boolean removeProductFromOffer(Integer offerId, Integer productId) {
        Optional<ProductOffer> productOfferOptional = productOfferRepository.findById(offerId);
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOfferOptional.isPresent() && productOptional.isPresent())
        {
            ProductOffer productOffer = productOfferOptional.get();
            Set<Product> productSet = productOffer.getProductList();
            Product product = productOptional.get();
            product.setDiscountedPrice(0.0);
            product.setProductOffer(null);
            productRepository.save(product);
            productSet.remove(product);
            productOfferRepository.save(productOffer);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    @Override
    public Integer totalSold(Product product, User user) {
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        int count=0;
        if(!purchaseOrderList.isEmpty())
        {
            for (PurchaseOrder purchaseOrder :purchaseOrderList) {
                for(OrderItem orderItem : purchaseOrder.getOrderItems()) {
                    if(orderItem.getProduct().equals(product) && orderItem.getOrderStatus().equals("delivered"))
                    {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public void updateIsEnabled() {
        List<ProductOffer> productOfferList = productOfferRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for(ProductOffer productOffer : productOfferList) {
            if (productOffer.isActive()) {
                boolean isBeforeOrEqualExpiryDate = currentDate.isBefore(productOffer.getExpiryDate()) || currentDate.isEqual(productOffer.getExpiryDate());
                boolean isAfterOrEqualStartDate = currentDate.isAfter(productOffer.getStartDate()) || currentDate.isEqual(productOffer.getStartDate());
                System.out.println("isBeforeOrEqualExpiry"+ isBeforeOrEqualExpiryDate);
                System.out.println("isAfterOrEqualStartDate"+ isAfterOrEqualStartDate);
                if (isBeforeOrEqualExpiryDate && isAfterOrEqualStartDate) {
                    System.out.println(productOffer.getProductOfferName());
                    productOffer.setEnabled(true);
                } else {
                    productOffer.setEnabled(false);
                }
                try {
                    productOfferRepository.save(productOffer);
                } catch (Exception e) {
                    e.printStackTrace(); // Log the exception
                }
               }
        }
    }

    @Override
    public boolean findProductNameExist(String productName) {
        return productRepository.existsByProductNameIgnoreCase(productName);
    }

    @Override
    public Product findByProductId(Integer id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.orElse(null);
    }

}
