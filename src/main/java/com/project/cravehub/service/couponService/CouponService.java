package com.project.cravehub.service.couponService;

import com.project.cravehub.dto.CouponDto;
import com.project.cravehub.model.user.Coupon;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

public interface CouponService {

    void saveCoupon(CouponDto couponDto);

    void inactiveCoupon(Integer id);

    void activeCoupon(Integer id);

    void saveCouponAndUser(String userEmail, Coupon coupon);

    boolean isCouponAlreadyUsed(String couponCode, String userEmail);


    double totalAmountPurchased(String userEmail);

    Coupon couponById(Integer couponId);

    boolean editCouponById(CouponDto couponDto, Integer id);

    List<Coupon> getAllValidCoupons(LocalDate date);
}
