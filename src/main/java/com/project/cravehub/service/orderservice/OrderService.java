package com.project.cravehub.service.orderservice;

import com.project.cravehub.model.user.OrderItem;

public interface OrderService {

    void cancelOrderAndRefund(OrderItem orderItem);
}
