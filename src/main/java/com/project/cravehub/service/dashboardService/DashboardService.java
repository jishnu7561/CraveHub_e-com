package com.project.cravehub.service.dashboardService;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;


public interface DashboardService {



    double totalRevenueWeekly(String period);

    Integer totalSales(String status);

    Integer totalStocks();

    Integer countOfCod();

    Integer countOfOnline();

    String getUserNameByEmail(String name);
}
