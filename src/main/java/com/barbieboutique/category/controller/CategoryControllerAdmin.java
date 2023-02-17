package com.barbieboutique.category.controller;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.category.service.CategoryService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.language.service.LanguageService;
import com.barbieboutique.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryControllerAdmin {
    private final CategoryService categoryService;
    private final LanguageService languageService;
    private final Utils utils;

    @Transactional
    @GetMapping()
    public String categories(Model model, HttpServletRequest request) {
//        List<Category> categories = categoryService.getALL();
        Language language = languageService.getByCode("ru");
        List<Category> categories = categoryService.findAllByParentCategoryId(null);

        model.addAttribute("categories", categories);
        model.addAttribute("language", language);

        return "admin-categories";
    }

    @Transactional
    @GetMapping("/{id}/new-category")
    public String newCategory(@PathVariable Long id, Model model) {
        Category category = new Category();
        category.setCategoryTitles(utils.translatorTemplate());

        if (id != 0){
            category.setParentCategory(categoryService.getById(id));
        }

        List<Category> categories = categoryService.getALL();
        Language language = languageService.getByCode("ru");

        model.addAttribute("categories", categories);
        model.addAttribute("language", language);
        model.addAttribute("category", category);

        return "new-category";
    }

    @Transactional
    @PostMapping
    public String addCategory(@ModelAttribute Category category, MultipartFile file) {
        categoryService.save(category, file);

        return "redirect:/admin/categories";
    }

    @Transactional
    @GetMapping("/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.getById(id);

        List<Category> subCategories = categoryService.findAllByParentCategoryId(id);

        List<Category> categories = categoryService.getALL();
        Language language = languageService.getByCode("ru");

        model.addAttribute("categories", categories);
        model.addAttribute("subCategories", subCategories);
        model.addAttribute("language", language);
        model.addAttribute("category", category);

        return "admin-category";
    }

    @PutMapping("/{id}")
    public String updateCategory(@ModelAttribute Category category, MultipartFile file) {
        if (category.getParentCategory() != null && category.getParentCategory().getId() == null){
            System.out.println("1");
            category.setParentCategory(null);
        }

        categoryService.save(category, file);

        return "redirect:/admin/categories";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Long id) {
       categoryService.deleteById(id);

        return "redirect:/admin/categories";
    }
}
