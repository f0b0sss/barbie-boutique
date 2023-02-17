package com.barbieboutique.card.controller;

import com.barbieboutique.card.entity.Bucket;
import com.barbieboutique.card.entity.Order;
import com.barbieboutique.card.service.BucketService;
import com.barbieboutique.card.service.OrderService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import com.barbieboutique.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final BucketService bucketService;
    private final Utils utils;

    @GetMapping
    public String orders(Model model, Principal principal) {
        if (principal == null) {
            return "login";
        } else {
            User user = userService.findByEmail(principal.getName());

            List<Order> orders = orderService.findAllByUserId(user.getId());

            model.addAttribute("orders", orders);

            return "orders";
        }
    }

    @Transactional
    @PostMapping
    public String newOrder(Principal principal) {
        User user = userService.findByEmail(principal.getName());

        Bucket bucket = user.getBucket();

        orderService.newOrder(bucket);

        return "redirect:/orders";
    }

    @Transactional
    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id, Model model) {
        Language language = utils.getCurrentLanguage();
        Order order = orderService.getById(id);

        model.addAttribute("language", language);
        model.addAttribute("order", order);

        return "order";
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);

        return "redirect:/orders";
    }

    @Transactional
    @DeleteMapping("/{id}/{product_id}")
    public String deleteProductFromOrder(@PathVariable Long id, @PathVariable Long product_id) {
        Order order = orderService.getById(id);

        orderService.deleteProductFromOrder(order, product_id);

        return "redirect:/orders/" + id;
    }


}
