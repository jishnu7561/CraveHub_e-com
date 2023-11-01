package com.project.cravehub.repository;

import com.project.cravehub.model.admin.Product;
import com.project.cravehub.model.user.OrderItem;
import com.project.cravehub.model.user.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem , Integer> {
    List<OrderItem> findByOrder(PurchaseOrder orderId);

    List<OrderItem> findByProduct(Product product);

    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.orderStatus = :status")
    int countByOrderStatus(@Param("status") String status);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderStatus = :status")
    List<OrderItem> findByOrderStatus(@Param("status") String status);
}
