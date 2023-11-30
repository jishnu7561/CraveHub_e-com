package com.project.cravehub.service.dashboardService;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.model.user.PurchaseOrder;
import com.project.cravehub.model.user.User;
import com.project.cravehub.repository.OrderItemRepository;
import com.project.cravehub.repository.ProductRepository;
import com.project.cravehub.repository.PurchaseOrderRepository;
import com.project.cravehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Component
public class DashboardServiceImpl implements DashboardService{

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public double totalRevenueWeekly(String period) {

        List<PurchaseOrder> purchaseOrdersList = new ArrayList<>();
        switch (period) {
            case "weekly=" -> {
                purchaseOrdersList = purchaseOrderRepository.findOrdersInCurrentWeek();

                System.out.println(period);
            }
            case "monthly=" -> {
                purchaseOrdersList = purchaseOrderRepository.findOrdersInCurrentMonth();
                System.out.println(period);
            }
            case "daily=" -> {
                purchaseOrdersList = purchaseOrderRepository.findOrdersForToday();
                System.out.println(period);
            }
            default -> {
                System.out.println(period);
                purchaseOrdersList = purchaseOrderRepository.findOrdersInCurrentYear();
            }
        }
        double totalCost = 0;
        int count =0;
        for(PurchaseOrder purchaseOrder : purchaseOrdersList)
        {
            for(OrderItem orderItem : purchaseOrder.getOrderItems()) {
                if(orderItem.getOrderStatus().equals("delivered")) {
                    System.out.println(orderItem.getOrderItemId());
                    System.out.println(purchaseOrder.getOrderId());

                    double cost = (orderItem.getItemCount() * orderItem.getProduct().getPrice()) * .6;
                    totalCost += cost;
                    System.out.println(totalCost+"------------");
                    count ++;
                }
            }
            if(purchaseOrder.getCoupon() != null && count>0 ) {
                totalCost = totalCost - purchaseOrder.getCoupon().getAmount();
                System.out.println(totalCost - purchaseOrder.getCoupon().getAmount()+"------------");

            }
        }
        System.out.println(totalCost);
        return totalCost;

    }

    @Override
    public Integer totalSales(String period) {
        List<PurchaseOrder> purchaseOrdersList = new ArrayList<>();
        switch (period) {
            case "weekly=" -> {
                purchaseOrdersList = purchaseOrderRepository.findOrdersInCurrentWeek();
                System.out.println(period);
            }
            case "monthly=" -> {
                purchaseOrdersList = purchaseOrderRepository.findOrdersInCurrentMonth();
                System.out.println(period);
            }
            case "daily=" -> {
                purchaseOrdersList = purchaseOrderRepository.findOrdersForToday();
                System.out.println(period);
            }
            default -> {
                System.out.println(period);
                purchaseOrdersList = purchaseOrderRepository.findOrdersInCurrentYear();
            }
        }
        int count = 0;
        for(PurchaseOrder purchaseOrder : purchaseOrdersList)
        {
            for(OrderItem orderItem : purchaseOrder.getOrderItems()) {
                if(orderItem.getOrderStatus().equals("delivered")) {
                   count ++ ;
                }
            }
        }

        return count;
    }

    @Override
    public Integer totalStocks() {
        List<Product> productList = productRepository.findAll();
        int count = 0;
        for(Product product : productList)
        {
            count += product.getQuantity();
        }
        return count;
    }

    @Override
    public Integer countOfCod() {
        List<PurchaseOrder> purchaseOrdersList = purchaseOrderRepository.findAll();
        int count =0;
        for(PurchaseOrder purchaseOrder : purchaseOrdersList) {
            if(purchaseOrder.getPaymentMethod().equals("COD")){
                count++;
            }
        }
        return count;
    }

    @Override
    public Integer countOfOnline() {
        List<PurchaseOrder> purchaseOrdersList = purchaseOrderRepository.findAll();
        int count =0;
        for(PurchaseOrder purchaseOrder : purchaseOrdersList) {
            if(purchaseOrder.getPaymentMethod().equals("online")){
                count++;
            }
        }
        return count;
    }

    @Override
    public String getUserNameByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user.getUserName();
    }

    public Integer calculateOrderTotalPrice(PurchaseOrder order)
    {
        int total=0 ;
        for(OrderItem orderItem : order.getOrderItems()){
            total += (int) (orderItem.getProduct().getPrice() * orderItem.getItemCount());
        }
        return total;
    }
}
