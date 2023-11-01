package com.project.cravehub.service.salesReportService;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.model.user.PurchaseOrder;
import com.project.cravehub.repository.OrderItemRepository;
import com.project.cravehub.repository.PurchaseOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SalesReportServiceImpl implements SalesReportService{

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Override
    public Map<String, String> getSalesDataForLastNPeriods(String period) {
        LocalDate currentDate = LocalDate.now();
        Map<String, String> salesData = new TreeMap<>();

        for (int i = 0; i < 7; i++) {
            String formattedPeriod = formatPeriod(currentDate, period);
            double salesAmount = calculateSalesForPeriod(currentDate, period);

            salesData.put("data"+i,formattedPeriod);
            salesData.put("dataValue"+i, String.valueOf(salesAmount));

            // Move to the previous period
            currentDate = moveBackOnePeriod(currentDate, period);
        }

        return salesData;
    }

    private String formatPeriod(LocalDate currentDate, String period) {
        DateTimeFormatter formatter;
        return switch (period) {
            case "day=" -> {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                yield currentDate.format(formatter);
            }
            case "month=" -> {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                yield currentDate.format(formatter);
            }
            case "year=" -> {
                formatter = DateTimeFormatter.ofPattern("yyyy");
                yield currentDate.format(formatter);
            }
            default -> throw new IllegalArgumentException("Invalid period");
        };
    }

    private double calculateSalesForPeriod(LocalDate currentDate, String period) {
        LocalDateTime startDateTime, endDateTime;

        if ("day=".equalsIgnoreCase(period)) {
            startDateTime = currentDate.atStartOfDay();
            endDateTime = currentDate.atTime(LocalTime.MAX);
        } else if ("month=".equalsIgnoreCase(period)) {
            startDateTime = currentDate.withDayOfMonth(1).atStartOfDay();
            endDateTime = currentDate.withDayOfMonth(currentDate.lengthOfMonth()).atTime(LocalTime.MAX);
        } else if ("year=".equalsIgnoreCase(period)) {
            startDateTime = currentDate.withDayOfYear(1).atStartOfDay();
            endDateTime = currentDate.withDayOfYear(currentDate.lengthOfYear()).atTime(LocalTime.MAX);
        } else {
            throw new IllegalArgumentException("Invalid period");
        }

        List<PurchaseOrder> orders = purchaseOrderRepository.findByOrderDateBetween(startDateTime, endDateTime);

        double totalSales = 0;

        for (PurchaseOrder purchaseOrder : orders) {
            for (OrderItem orderItem : purchaseOrder.getOrderItems()) {
                if (orderItem.getOrderStatus().equals("delivered")) {
                    System.out.println(orderItem.getOrderItemId());
                    totalSales++;
                }
            }
        }

        return totalSales;
    }

    private LocalDate moveBackOnePeriod(LocalDate currentDate, String period) {
        switch (period) {
            case "day=":
                return currentDate.minusDays(1);
            case "month=":
                return currentDate.minusMonths(1);
            case "year=":
                return currentDate.minusYears(1);
            default:
                throw new IllegalArgumentException("Invalid period");
        }
    }

    @Override
    public Map<Product, Integer> getSalesProfit() {
        Map<Product,Integer> profitData = new HashMap<>();

        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();

        for (PurchaseOrder purchaseOrder : purchaseOrderList) {
            for (OrderItem orderItem : purchaseOrder.getOrderItems()) {
                if (orderItem.getOrderStatus().equals("delivered")) {
                    Product product = orderItem.getProduct();
                    double amount = (orderItem.getItemCount() * product.getPrice()) * 0.6;

                    // Deduct coupon amount if it exists for this order
                    if (purchaseOrder.getCoupon() != null) {
                        amount -= purchaseOrder.getCoupon().getAmount();
                    }

                    // Ensure the amount is non-negative after applying the coupon
                    amount = Math.max(amount, 0);

                    String amountString = String.valueOf(amount);

                    // Convert the amount string to double and add it to the existing total for the product
                    int existingTotal = profitData.getOrDefault(product, 0);
                    double amountDouble = Double.parseDouble(amountString);
                    int newTotal = existingTotal + (int) amountDouble;
                    profitData.put(product, newTotal);
                }
            }
        }




//        for (PurchaseOrder purchaseOrder : purchaseOrderList) {
//            for (OrderItem orderItem : purchaseOrder.getOrderItems()) {
//                if (orderItem.getOrderStatus().equals("delivered")){
//                    Product product = orderItem.getProduct();
//                    double amount = (orderItem.getItemCount() * product.getPrice()) * 0.6;
//
//                    String amountString = String.valueOf(amount);
//
//                    // Convert the amount string to double and add it to the existing total for the product
//                    int existingTotal = profitData.getOrDefault(product, 0);
//                    double amountDouble = Double.parseDouble(amountString);
//                    int newTotal = existingTotal + (int) amountDouble;
//                    profitData.put(product, newTotal);
//                }
//            }
//        }


        return profitData;
    }

    @Override
    public List<OrderItem> getSalesReport() {
        return orderItemRepository.findByOrderStatus("delivered");
    }

    @Override
    public List<OrderItem> generateSalesReport(LocalDate start, LocalDate end) {
        LocalDateTime startOfDay = start.atStartOfDay(); // Start of the day
        LocalDateTime endOfDay = end.atTime(LocalTime.MAX); // End of the day
        List<OrderItem> orderItemList = new ArrayList<>();
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findByOrderDateBetween(startOfDay,endOfDay);
        for(PurchaseOrder purchaseOrder : purchaseOrderList)
        {
            for(OrderItem orderItem : purchaseOrder.getOrderItems())
            {
                if(orderItem.getOrderStatus().equals("delivered"))
                {
                    orderItemList.add(orderItem);
                }
            }
        }
        return orderItemList;
    }


}
