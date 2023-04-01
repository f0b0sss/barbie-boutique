package com.barbieboutique.bucket.repository;

import com.barbieboutique.bucket.entity.Order;
import com.barbieboutique.bucket.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long id);

    List<Order> findAllByStatus(OrderStatus status);

    @Modifying
    @Query("update Order set status = ?1 where id = ?2")
    void updateStatus(OrderStatus status, Long id);
}
