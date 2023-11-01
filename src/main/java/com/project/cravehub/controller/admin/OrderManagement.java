package com.project.cravehub.controller.admin;

import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.model.user.PurchaseOrder;
import com.project.cravehub.repository.OrderItemRepository;
import com.project.cravehub.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class OrderManagement {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @GetMapping("/listOrders")
    public String listOrders(Model model) {
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        Collections.reverse(orderItemList);
        model.addAttribute("orderItemList",orderItemList);
        return "OrderManagement";
    }

    @GetMapping("/orderDetails/{orderItemId}")
    public String orderDetails(@PathVariable Integer orderItemId,Model model) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);
        if(orderItemOptional.isPresent()) {
            OrderItem orderItem = orderItemOptional.get();
            model.addAttribute("orderItem",orderItem);
            return "orderDetails";
        }
        return "redirect:/listOrders";
    }
    @PostMapping("/updateOrderSts/{orderItemId}")
    public  String updateOrderSts(@RequestParam("selectedStatus") String selectedStatus,@PathVariable Integer orderItemId) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);
        System.out.println("in controller");
        System.out.println(selectedStatus);
        if(orderItemOptional.isPresent()) {
            OrderItem orderItem = orderItemOptional.get();
            orderItem.setOrderStatus(selectedStatus);
            String delivered = "delivered";
            if(selectedStatus.equals(delivered))
            {
                PurchaseOrder purchaseOrder = orderItem.getOrder();
                purchaseOrder.setPaymentStatus("success");
                purchaseOrderRepository.save(purchaseOrder);
                System.out.println("deli.....");
            }
            PurchaseOrder purchaseOrder = orderItem.getOrder();
            purchaseOrder.setPaymentStatus("pending");
            purchaseOrderRepository.save(purchaseOrder);
            orderItemRepository.save(orderItem);
            return "redirect:/admin/orderDetails/{orderItemId}?successful";

        }
        return "redirect:/admin/orderDetails?error";
    }

    @GetMapping("/cancelOrderDetails/{orderItemId}")
    public String cancelOrderItem(@PathVariable Integer orderItemId) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(orderItemId);
        if(orderItemOptional.isPresent())
        {
            OrderItem orderItem = orderItemOptional.get();
            orderItem.setOrderStatus("cancelled");
            orderItemRepository.save(orderItem);
            return "redirect:/admin/orderDetails/{orderItemId}?cancelled";
        }
        return "redirect:/admin/orderAdmin?error";
    }
}
