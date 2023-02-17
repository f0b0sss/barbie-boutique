package com.barbieboutique.controller;


import com.barbieboutique.category.service.CategoryService;
import com.barbieboutique.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping()
    public String index() {
        return "admin-main";
    }


}
