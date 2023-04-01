package com.barbieboutique.bucket.service;


import com.barbieboutique.bucket.entity.Bucket;
import com.barbieboutique.bucket.entity.Delivery;
import com.barbieboutique.bucket.entity.Order;
import com.barbieboutique.bucket.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    List<Order> findAllByUserId(Long user_id);

    List<Order> findAllByStatus(OrderStatus status);

    void newOrder(Bucket bucket);

    Order getById(Long id);

    void save(Order order);

    void updateStatus(OrderStatus status, Long id);

    void deleteById(Long id);

    void deleteProductFromOrder(Order order, Long product_id);

    void sendOrderDetailsToEmail(Delivery delivery);



}
