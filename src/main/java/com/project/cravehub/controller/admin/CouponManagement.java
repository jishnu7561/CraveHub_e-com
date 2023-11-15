package com.project.cravehub.controller.admin;

import com.project.cravehub.dto.CouponDto;
import com.project.cravehub.model.user.Coupon;
import com.project.cravehub.repository.CouponRepository;
import com.project.cravehub.service.couponService.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
    public String listCoupons(Model model,HttpSession session) {
        List<Coupon> coupons = couponRepository.findAll();
        model.addAttribute("coupons",coupons);
        model.addAttribute("couponSuccess",(String)session.getAttribute("couponSuccess"));
        model.addAttribute("couponError",(String)session.getAttribute("couponError"));

        session.removeAttribute("couponError");
        session.removeAttribute("couponSuccess");

        return "list-coupon";
    }

    @GetMapping("/addCoupon")
    public String addCouponsGet() {
        return "add-coupon";
    }

    
    @PostMapping("/addCoupon")
    public String addCouponPost(@ModelAttribute("coupon") CouponDto couponDto,
                                HttpSession session) {
        System.out.println("in coupon controller");
        couponService.saveCoupon(couponDto);
        System.out.println("saved");

        session.setAttribute("couponSuccess","Coupon added successfully ");
        return "redirect:/admin/listCoupons";
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

    @GetMapping("/editCoupon/{couponId}")
    public String editCouponGet(@PathVariable("couponId") Integer id, Model model) {
        model.addAttribute("couponId", id);
        Optional<Coupon> couponOptional = couponRepository.findById(id);
        couponOptional.ifPresent(coupon -> model.addAttribute("coupon", coupon));
        model.addAttribute("error","Something went wrong ");
        return "edit_coupon";
    }

    @PostMapping("/editCoupon/{couponId}")
    public String editCouponPost(@PathVariable("couponId") Integer id,
                                 @ModelAttribute("coupon") CouponDto couponDto,
                                 HttpSession session) {
        boolean added = couponService.editCouponById(couponDto,id);
        if(added)
        {
            session.setAttribute("couponSuccess","Coupon details edited successfully ");
            return "redirect:/admin/listCoupons";
        }
        session.setAttribute("couponError","Failed to edit coupon details ");
        return "redirect:/admin/listCoupons";
    }
}
