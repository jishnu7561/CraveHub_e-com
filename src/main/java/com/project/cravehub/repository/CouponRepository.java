package com.project.cravehub.repository;

import com.project.cravehub.model.user.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {

    Coupon findByCouponCode(String couponCode);

    boolean existsByCouponCodeAndUserEmail(String couponCode, String email);

//    @Query("SELECT COUNT(c.users) FROM Coupon c WHERE c.couponCode = :couponCode")
//    int countUsersByCouponCode(@Param("couponCode") String couponCode);
int countByCouponCodeAndUserIsNotNull(String couponCode);
}
