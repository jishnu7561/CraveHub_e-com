package com.project.cravehub.service.couponService;

import com.project.cravehub.dto.CouponDto;
import com.project.cravehub.model.user.Coupon;

public interface CouponService {

    void saveCoupon(CouponDto couponDto);

    void inactiveCoupon(Integer id);

    void activeCoupon(Integer id);

    void saveCouponAndUser(String userEmail, Coupon coupon);

    boolean isCouponAlreadyUsed(String couponCode, String userEmail);


    double totalAmountPurchased(String userEmail);

    Coupon couponById(Integer couponId);
}
