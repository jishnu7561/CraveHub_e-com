package com.project.cravehub.repository;

import com.project.cravehub.model.user.Address;
import com.project.cravehub.model.user.PurchaseOrder;
import com.project.cravehub.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Integer> {
    List<PurchaseOrder> findByUser(User user);

    boolean existsByAddress(Address address);

    List<PurchaseOrder> findByOrderDateBetween(LocalDateTime startOfWeek, LocalDateTime endOfWeek);

    @Query("SELECT po FROM PurchaseOrder po WHERE YEARWEEK(po.orderDate) = YEARWEEK(NOW())")
    List<PurchaseOrder> findOrdersInCurrentWeek();

    @Query("SELECT po FROM PurchaseOrder po WHERE MONTH(po.orderDate) = MONTH(NOW()) AND YEAR(po.orderDate) = YEAR(NOW())")
    List<PurchaseOrder> findOrdersInCurrentMonth();


    @Query("SELECT po FROM PurchaseOrder po WHERE YEAR(po.orderDate) = YEAR(NOW())")
    List<PurchaseOrder> findOrdersInCurrentYear();


    @Query("SELECT po FROM PurchaseOrder po WHERE DATE(po.orderDate) = CURRENT_DATE()")
    List<PurchaseOrder> findOrdersForToday();

    @Query("SELECT p FROM PurchaseOrder p WHERE DATE_FORMAT(p.orderDate, '%Y-%m-%d') LIKE %?1%")
    List<PurchaseOrder> findByOrderDateContaining(String date);



}
