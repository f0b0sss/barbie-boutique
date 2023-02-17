package com.barbieboutique.card.service;


import com.barbieboutique.card.entity.Bucket;
import com.barbieboutique.card.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> findAllByUserId(Long user_id);

    List<Order> findAllByStatus(String status);

    void newOrder(Bucket bucket);

    Order getById(Long id);

    void deleteById(Long id);

    void deleteProductFromOrder(Order order, Long product_id);



}
