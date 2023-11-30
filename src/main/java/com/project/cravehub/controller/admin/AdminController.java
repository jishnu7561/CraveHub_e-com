package com.project.cravehub.controller.admin;

import com.project.cravehub.dto.ProductDto;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.model.user.PurchaseOrder;
import com.project.cravehub.repository.OrderItemRepository;
import com.project.cravehub.repository.PurchaseOrderRepository;
import com.project.cravehub.service.dashboardService.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public AdminController() {
    }

    @GetMapping("/adminHome")
    public String showAdminHome(Model model, HttpSession session, Principal principal) {
        String period = "yearly";
        int totalRevenue = (int)dashboardService.totalRevenueWeekly(period);
        String status = "delivered";
        Integer totalSales = dashboardService.totalSales(period);
        Integer totalStocks = dashboardService.totalStocks();
        model.addAttribute("sales",totalSales);
        model.addAttribute("revenue",totalRevenue);
        model.addAttribute("stocks",totalStocks);

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        Collections.reverse(orderItemList);
        model.addAttribute("orderList",orderItemList);

        Integer cod = dashboardService.countOfCod();
        Integer online = dashboardService.countOfOnline();
        model.addAttribute("cod",cod);
        model.addAttribute("online",online);

        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        Collections.reverse(purchaseOrderList);
        model.addAttribute("order",purchaseOrderList);
        String userName = dashboardService.getUserNameByEmail(principal.getName());
        model.addAttribute("userName",userName);
        session.setAttribute("userName",userName);
        return "admin-index";
    }

    @PostMapping("/adminHome")
    @ResponseBody
    public Map<String, Object> adminHome(@RequestBody String period)
    {
        System.out.println("controller called"  + period);
        int totalRevenue = (int)dashboardService.totalRevenueWeekly(period);
        String status = "delivered";
        Integer totalSales = dashboardService.totalSales(period);
        Integer totalStocks = dashboardService.totalStocks();
        Map<String, Object> response = new HashMap<>();
        response.put("sales", totalSales);
        response.put("revenue", totalRevenue);
        response.put("stocks", totalStocks);
        return response;
    }
}
