package com.project.cravehub.controller.user;

import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.repository.ReviewRepository;
import com.project.cravehub.service.reviewService.ReviewService;
import com.project.cravehub.service.userservice.UserService;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;
    @GetMapping("/addReview/{orderItemId}")
    public String addReview(@PathVariable("orderItemId") OrderItem orderItem,
                            Model model,Principal principal,HttpSession session) {
        boolean isAlreadyDone = reviewService.existsByUserAndOrderItemAndProduct(principal, orderItem);
        if(isAlreadyDone)
        {
            System.out.println("already done :");
            session.setAttribute("reviewError","you have already done");
            return "redirect:/orders";
        }
        System.out.println("orderItem in review :" +orderItem);
        model.addAttribute("orderItem",orderItem);


        return "addReview";
    }

    @GetMapping("/addReview")
    public String addReview(@RequestParam("rating") Integer rating,
                            @RequestParam("comment") String comment,
                            @RequestParam("productId") Integer productId,
                            @RequestParam("orderItemId") OrderItem orderItem,
                            HttpSession session, Principal principal) {
        System.out.println("the review rating is "+rating);
        System.out.println("the comment is "+ comment);
        System.out.println("productId selected is "+productId);

        boolean added = reviewService.addReview(productId,principal,rating,comment,orderItem);
        if(added)
        {
            session.setAttribute("reviewSuccessful","Review successfully added");
        }else {
            session.setAttribute("reviewError","Failed to added review");
        }
        return "redirect:/orders";

    }
}
