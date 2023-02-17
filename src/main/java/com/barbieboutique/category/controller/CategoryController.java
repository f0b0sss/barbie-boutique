package com.barbieboutique.category.controller;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.category.service.CategoryService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.service.ProductService;
import com.barbieboutique.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final Utils utils;
    private final ProductService productService;


    @Transactional
    @GetMapping
    public String categories(Model model, HttpServletRequest request, HttpSession httpSession) {
//        List<Category> categories = categoryService.getALL().stream()
//                .filter(category -> category.getParentCategory() == null)
//                .collect(Collectors.toList());
        Language language = utils.getCurrentLanguage();
        List<Category> categories = categoryService.findAllByParentCategoryId(null);
//        System.out.println(categories.size());


//
//        System.out.println(language.getCode());
//
//        for (Category category : categories) {
//            System.out.println(category.getCategoryTitles().get(language));
//        }

        model.addAttribute("categories", categories);
        model.addAttribute("language", language);

        return "categories";
    }



    @Transactional
    @GetMapping("/{id}")
    public String category(@PathVariable Long id, Model model) {
        Category category = categoryService.getById(id);
        Language language = utils.getCurrentLanguage();

        List<Category> categories = categoryService.findAllByParentCategoryId(id);
        List<Product> products;

        if (categories.isEmpty()){
            products = productService.findAllByCategories(category);
        }else {
            products = productService.findAllByCategoriesIn(categories);
        }

        model.addAttribute("language", language);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        return "category";
    }





}
