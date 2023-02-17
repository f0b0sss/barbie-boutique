package com.barbieboutique.card.repository;

import com.barbieboutique.card.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long id);

    List<Order> findAllByStatus(String status);

}
