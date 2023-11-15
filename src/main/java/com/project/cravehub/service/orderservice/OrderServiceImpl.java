package com.project.cravehub.service.orderservice;

import com.project.cravehub.model.user.*;
import com.project.cravehub.repository.PurchaseOrderRepository;
import com.project.cravehub.repository.UserRepository;
import com.project.cravehub.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Override
    public void cancelOrderAndRefund(OrderItem orderItem) {
//
//        Transactions transactions = new Transactions();
//        Set<User> user = new HashSet<>();
//        user.add(orderItem.getOrder().getUser());
//        transactions.setUser(user);
//        LocalDateTime date = LocalDateTime.now();
//        transactions.setTransactionDate(date);
//        Double discount =0.0;
//        if(!orderItem.getOrder().isRefund_used())
//        {
//            discount = orderItem.getOrder().getCoupon().getAmount();
//        }
//        transactions.setOrderAmount(orderItem.getOrder().getOrderAmount()-discount);
//        transactions.setPaymentMethod(orderItem.getOrder().getPaymentMethod());
//        transactions.setPurchaseOrder(orderItem.getOrder());
//        transactions.setPurpose("refund");
//        transactionRepository.save(transactions);
//
//        User user = orderItem.getOrder().getUser();
//        Wallet wallet = user.getWallet();
//        if(wallet == null)
//        {
//            wallet = new Wallet();
//            wallet.setUser(user);
//            user.setWallet(wallet);
//        }
//        wallet.setBalance(wallet.getBalance()+((orderItem.getItemCount() * orderItem.getProduct().getPrice())- discount));
//        PurchaseOrder purchaseOrder = orderItem.getOrder();
//        purchaseOrder.setRefund_used(true);
//        purchaseOrderRepository.save(purchaseOrder);
//        walletRepository.save(wallet);
//        userRepository.save(user);
    }
}
