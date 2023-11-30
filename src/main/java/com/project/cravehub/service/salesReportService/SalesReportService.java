package com.project.cravehub.service.salesReportService;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.model.user.PurchaseOrder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SalesReportService {

//    Map<String, Double> getSalesDataForLast7Days();

    Map<String, String> getSalesDataForLastNPeriods(String daily);

    Map<Product, Integer> getSalesProfit();

    List<OrderItem> getSalesReport();

    List<OrderItem> generateSalesReport(LocalDate start, LocalDate end);

    double totalRevenue(LocalDate dateFrom, LocalDate dateTo);

    int totalSales(LocalDate dateFrom, LocalDate dateTo);

    double getTotalRevenue();

    int getTotalSales();

//    void downloadAsPdf(List<OrderItem> orderItemList);

//    void downloadAsPdf(HttpServletResponse response,List<OrderItem> orderItemList) throws IOException;


//    List<OrderItem> generateSalesReport(LocalDate startDate, LocalDate endDate);
}
