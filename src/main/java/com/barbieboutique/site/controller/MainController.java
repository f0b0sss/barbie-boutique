package com.barbieboutique.site.controller;

import com.barbieboutique.language.entity.Language;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.service.ProductService;
import com.barbieboutique.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
public class MainController {

    @Autowired
    private ProductService productService;
    @Autowired
    private Utils utils;


    @RequestMapping({"", "/"})
    public String index(Model model, HttpSession httpSession, HttpServletRequest request) {
        Language language = utils.getCurrentLanguage();
        int currentPage = 1;
        int pageSize = 8;

        Page<Product> productPage = productService.findAll(PageRequest.of(currentPage - 1, pageSize));

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("language", language);

        return "index";
    }

}
