package com.barbieboutique.card.controller;

import com.barbieboutique.card.entity.Order;
import com.barbieboutique.card.entity.OrderStatus;
import com.barbieboutique.card.service.OrderService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@AllArgsConstructor
public class OrderControllerAdmin {
    private final OrderService orderService;
    private final Utils utils;


    @GetMapping
    public String orders(Model model, @RequestParam String status) {
        List<Order> orders = orderService.findAllByStatus(OrderStatus.valueOf(status));
        OrderStatus[] orderStatuses = OrderStatus.values();

        model.addAttribute("orderStatuses", orderStatuses);
        model.addAttribute("orders", orders);

        return "admin-orders";
    }

    @GetMapping("/{id}")
    public String order(@PathVariable Long id, Model model) {
        Language language = utils.getCurrentLanguage();
        Order order = orderService.getById(id);
        OrderStatus[] orderStatuses = OrderStatus.values();

        model.addAttribute("language", language);
        model.addAttribute("orderStatuses", orderStatuses);
        model.addAttribute("order", order);

        return "admin-order";
    }

    @Transactional
    @PutMapping("/{id}")
    public String update(@ModelAttribute Order order, @PathVariable Long id) {
        orderService.updateStatus(order.getStatus(), id);

        return "redirect:/admin/orders?status=CREATED";
    }






}
