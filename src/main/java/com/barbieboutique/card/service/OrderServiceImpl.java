package com.barbieboutique.card.service;


import com.barbieboutique.card.entity.Bucket;
import com.barbieboutique.card.entity.Order;
import com.barbieboutique.card.entity.OrderDetails;
import com.barbieboutique.card.entity.OrderStatus;
import com.barbieboutique.card.repository.BucketRepository;
import com.barbieboutique.card.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BucketRepository bucketRepository;

    @Override
    public List<Order> findAllByUserId(Long user_id) {
        return orderRepository.findAllByUserId(user_id);
    }

    @Override
    public List<Order> findAllByStatus(String status) {
        return orderRepository.findAllByStatus(status);
    }

    @Override
    public void newOrder(Bucket bucket) {
        List<OrderDetails> details = getDetails(bucket);

        BigDecimal sum = details.stream()
                .map(OrderDetails::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(bucket.getUser())
                .details(details)
                .sum(sum)
                .status(OrderStatus.NEW)
                .build();

        orderRepository.save(order);

        bucket.getProducts().clear();

        bucketRepository.save(bucket);

    }

    private List<OrderDetails> getDetails(Bucket bucket) {
        List<OrderDetails> details = new ArrayList<>();

        bucket.getProducts().stream()
                .forEach(product -> {
                    OrderDetails orderDetails = OrderDetails.builder()
                            .product(product)
                            .price(product.getPrice())
                            .build();

                    details.add(orderDetails);
                });

        return details;
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteProductFromOrder(Order order, Long product_id) {
        List<OrderDetails> orderDetails = order.getDetails().stream()
                .filter(d -> d.getId() != product_id)
                .collect(Collectors.toList());

        order.setDetails(orderDetails);

        orderRepository.save(order);
    }
}
