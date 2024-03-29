package com.barbieboutique.bucket.controller;

import com.barbieboutique.bucket.entity.Bucket;
import com.barbieboutique.bucket.entity.Delivery;
import com.barbieboutique.bucket.entity.Order;
import com.barbieboutique.bucket.entity.OrderStatus;
import com.barbieboutique.bucket.service.OrderService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.site.entity.Notification;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import com.barbieboutique.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private Utils utils;
    @Autowired
    private MessageSource messages;
    @Autowired
    private JavaMailSender mailSender;

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

        Order order = orderService.findAllByUserId(user.getId()).stream()
                .sorted(Comparator.comparing(Order::getCreated).reversed()).findFirst().get();

        return "redirect:/orders/" + order.getId() + "/checkout";
    }

    @Transactional
    @GetMapping("/{id}")
    public String order(@PathVariable Long id, Model model) {
        Language language = utils.getCurrentLanguage();
        Order order = orderService.getById(id);

        model.addAttribute("language", language);
        model.addAttribute("order", order);

        return "order";
    }

    @GetMapping("/{id}/checkout")
    public String orderCheckout(Principal principal, @PathVariable Long id, Model model) throws IOException {
        Language language = utils.getCurrentLanguage();
        Order order = orderService.getById(id);

        Delivery delivery = new Delivery();
        delivery.setOrder(order);

        model.addAttribute("language", language);
        model.addAttribute("order", order);
        model.addAttribute("delivery", delivery);

        return "checkout";
    }

    @Transactional
    @PostMapping("/{id}/checkout")
    public String checkout(@ModelAttribute Delivery delivery, @PathVariable Long id) {
//        orderService.sendOrderDetailsToEmail(delivery);

        delivery.setOrder(orderService.getById(id));

        Locale locale = LocaleContextHolder.getLocale();

        String subject = messages.getMessage("checkout.email.subject", null, locale);

        User user = delivery.getOrder().getUser();

        String deliveryMessageClient = buildDeliveryInfoClient(delivery, locale);
        String deliveryMessageAdmin = buildDeliveryInfoAdmin(delivery);

        System.out.println(delivery.getPostDetails().getPostIndex());
        System.out.println(delivery.getPostDetails().getCity());

        orderService.updateStatus(OrderStatus.CREATED, delivery.getOrder().getId());
        mailSender.send(constructOrderEmail(subject, deliveryMessageClient, user));

        User admin = new User();
        admin.setEmail("lizamatveykina@gmail.com");
        mailSender.send(constructOrderEmail(subject, deliveryMessageAdmin, admin));

        return "redirect:/orders";
    }

    private String buildDeliveryInfoClient(Delivery delivery, Locale locale) {
        Long orderN = delivery.getOrder().getId();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String created = delivery.getOrder().getCreated().format(formatter);

        BigDecimal sum = delivery.getOrder().getSum();

        String message = messages.getMessage("checkout.email.message", null, locale);

        return message + "\n\n" +
                "orderN: " + orderN + "\n" +
                "created : " + created + "\n" +
                "sum: " + sum + "\n";
    }

    private String buildDeliveryInfoAdmin(Delivery delivery) {
        Long orderN = delivery.getOrder().getId();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String created = delivery.getOrder().getCreated().format(formatter);

        BigDecimal sum = delivery.getOrder().getSum();

        return "New Order" + "\n\n" +
                "orderN: " + orderN + "\n" +
                "created : " + created + "\n" +
                "email : " + delivery.getOrder().getUser().getEmail() + "\n" +
                "firstname : " + delivery.getOrder().getUser().getFirstname() + "\n" +
                "lastname : " + delivery.getOrder().getUser().getLastname() + "\n" +
                "phone : " + delivery.getOrder().getUser().getPhone() + "\n" +
                "post method : " + delivery.getPostDetails().getPostMethod() + "\n" +
                "city : " + delivery.getPostDetails().getCity() + "\n" +
                "post index : " + delivery.getPostDetails().getPostIndex() + "\n" +
                "address : " + delivery.getPostDetails().getAddress() + "\n" +
                "created : " + created + "\n" +
                "sum: " + sum + "\n";
    }

    private SimpleMailMessage constructOrderEmail(String subject, String deliveryMessage, User user) {
        return constructEmail(subject, " \r\n" + deliveryMessage, user);
    }

    private SimpleMailMessage constructEmail(String subject, String message, User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(message);
        email.setTo(user.getEmail());
        email.setFrom(System.getProperty("mail.username"));

        return email;
    }



    @GetMapping("/checkout-success")
    public String registrationNotification(Model model) {
        Locale locale = LocaleContextHolder.getLocale();

        String title = messages.getMessage("auth.message.regSuccess", null, locale);
        String header = messages.getMessage("auth.message.regSuccess", null, locale);
        String message = messages.getMessage("auth.message.sendToken", null, locale);

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setHeader(header);
        notification.setMessage(message);

        model.addAttribute("notification", notification);
        return "notification";
    }

    @Transactional
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
