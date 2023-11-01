package com.project.cravehub.controller.admin;

import com.project.cravehub.dto.CouponDto;
import com.project.cravehub.model.user.Coupon;
import com.project.cravehub.repository.CouponRepository;
import com.project.cravehub.service.couponService.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CouponManagement {

    @Autowired
    private CouponRepository couponRepository;

    @ModelAttribute("coupon")
    private CouponDto couponDto() {
        return new CouponDto();
    }
    @Autowired
    private CouponService couponService;

    @GetMapping("/listCoupons")
    public String listCoupons(Model model) {
        List<Coupon> coupons = couponRepository.findAll();
        model.addAttribute("coupons",coupons);
        return "list-coupon";
    }

    @GetMapping("/addCoupon")
    public String addCouponsGet() {
        return "add-coupon";
    }




    @PostMapping("/addCoupon")
    public String addCouponPost(@ModelAttribute("coupon") CouponDto couponDto) {
        System.out.println("in coupon controller");
            couponService.saveCoupon(couponDto);
        System.out.println("saved");

        return "redirect:/admin/addCoupon?successful";
    }

    @GetMapping ("/inactive/{id}")
    public String inactive(@PathVariable Integer id)
    {
        couponService.inactiveCoupon(id);
        return "redirect:/admin/listCoupons";

    }

    @GetMapping("/active/{id}")
    public String active(@PathVariable Integer id)
    {
        couponService.activeCoupon(id);

        return "redirect:/admin/listCoupons";
    }


}
