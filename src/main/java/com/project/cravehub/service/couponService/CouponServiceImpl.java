package com.project.cravehub.service.couponService;

import com.project.cravehub.dto.CouponDto;
import com.project.cravehub.model.user.Cart;
import com.project.cravehub.model.user.CartItem;
import com.project.cravehub.model.user.Coupon;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.CartItemRepository;
import com.project.cravehub.repository.CartRepository;
import com.project.cravehub.repository.CouponRepository;
import com.project.cravehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CouponServiceImpl implements CouponService{

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public void saveCoupon(CouponDto couponDto) {
        Coupon coupon = new Coupon(
                couponDto.getCouponCode(),
                couponDto.getDescription(),
                couponDto.getExpiryDateAsLocalDate(),
                couponDto.getAmount(),
                couponDto.getUsageCount(),
                couponDto.getMinimumPurchaseAmount());

        couponRepository.save(coupon);
        System.out.println("saved");
    }

    @Override
    public void inactiveCoupon(Integer id) {
        Coupon inactivateCoupon = couponRepository.findById(id).get();
        inactivateCoupon.setActive(false);
        couponRepository.save(inactivateCoupon);
    }

    @Override
    public void activeCoupon(Integer id) {
        Coupon activateCoupon = couponRepository.findById(id).get();
        activateCoupon.setActive(true);
        couponRepository.save(activateCoupon);
    }

    @Override
    public boolean isCouponAlreadyUsed(String couponCode, String userEmail) {
        return couponRepository.existsByCouponCodeAndUserEmail(couponCode, userEmail);
    }

    @Override
    public double totalAmountPurchased(String userEmail) {
       User user = userRepository.findByEmail(userEmail);
        Cart cart = cartRepository.findByUser(user);
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        double totalAmount = 0;
        for (CartItem cartItem : cartItems)
        {
           double itemPrice = cartItem.getQuantity() * cartItem.getPrice();
           totalAmount += itemPrice;
        }
        return totalAmount;
    }

    @Override
    public Coupon couponById(Integer couponId) {
        if(couponId!=null) {
            Optional<Coupon> couponOptional = couponRepository.findById(couponId);
            return couponOptional.orElse(null);
        }
        return null;
    }

    @Transactional
    public void saveCouponAndUser(String userEmail, Coupon coupon) {
        User user = userRepository.findByEmail(userEmail);
        coupon.addUser(user);
        couponRepository.save(coupon);
    }

    @Override
    public boolean editCouponById(CouponDto couponDto, Integer id) {
        Optional<Coupon> couponOptional = couponRepository.findById(id);
        if (couponOptional.isPresent())
        {
            couponOptional.get().setCouponCode(couponDto.getCouponCode());
            couponOptional.get().setAmount(couponDto.getAmount());
            couponOptional.get().setExpiryDate(LocalDate.parse(couponDto.getExpiryDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            couponOptional.get().setMinimumPurchaseAmount(couponDto.getMinimumPurchaseAmount());
            couponOptional.get().setUsageCount(couponDto.getUsageCount());
            couponOptional.get().setDescription(couponDto.getDescription());
            couponRepository.save(couponOptional.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Coupon> getAllValidCoupons(LocalDate date) {
        return couponRepository.findByExpiryDateGreaterThanEqual(date);
    }

}
