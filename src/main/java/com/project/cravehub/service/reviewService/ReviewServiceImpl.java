package com.project.cravehub.service.reviewService;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.model.user.Review;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.ReviewRepository;
import com.project.cravehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public boolean addReview(Integer productId, Principal principal,Integer rating,String comment,OrderItem orderItem) {
        User user = userRepository.findByEmail(principal.getName());
        Optional<Product> product = productRepository.findById(productId);
        if(product.isPresent())
        {
            Review review = new Review();
            review.setProduct(product.get());
            review.setUser(user);
            review.setComment(comment);
            review.setRating(rating);
            review.setOrderItem(orderItem);
            product.get().addReview(review);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByUserAndOrderItemAndProduct(Principal principal, OrderItem orderItem) {
        Product product = orderItem.getProduct();
        User user = userRepository.findByEmail(principal.getName());
       return reviewRepository.existsByUserAndOrderItemAndProduct(user, orderItem, product);
    }

    public Integer getReviewCountForProduct(Product product) {
        return reviewRepository.countByProduct(product);
    }
}
