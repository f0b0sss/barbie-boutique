package com.barbieboutique.card.service;


import com.barbieboutique.card.entity.Bucket;
import com.barbieboutique.card.entity.Order;
import com.barbieboutique.card.entity.OrderStatus;

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



}
