package com.barbieboutique.product.controller;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.category.service.CategoryService;
import com.barbieboutique.filter.entity.Filter;
import com.barbieboutique.filter.service.FilterService;
import com.barbieboutique.image.service.ImageService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.language.service.LanguageService;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.service.ProductService;
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
@RequestMapping("/admin/products")
public class ProductControllerAdmin {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final FilterService filterService;
    private final LanguageService languageService;
    private final ImageService imageService;
    private final Utils utils;


    @Transactional
    @GetMapping
    public String products(Model model, HttpServletRequest request) {
        List<Product> products = productService.findAll();
        Language language = languageService.getByCode("ru");

        model.addAttribute("products", products);
        model.addAttribute("language", language);

        return "admin-products";
    }

    @GetMapping("/new-product")
    public String newProduct(Model model) {
        Product product = new Product();
        product.setProductTitles(utils.translatorTemplate());

        List<Category> categories = categoryService.getALL();
        List<Filter> filters = filterService.getALL();
        Language language = languageService.getByCode("ru");

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("filters", filters);
        model.addAttribute("language", language);

        return "new-product";
    }

    @PostMapping
    public String addProduct(@ModelAttribute Product product, MultipartFile[] files) {

        productService.save(product, files);

        return "redirect:/admin/products";
    }

    @Transactional
    @GetMapping("/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        Language language = languageService.getByCode("ru");
        List<Filter> filters = filterService.getALL();
        List<Category> allCategories = categoryService.getALL();

        model.addAttribute("product", product);
        model.addAttribute("allCategories", allCategories);
        model.addAttribute("images", product.getImages());
        model.addAttribute("filters", filters);
        model.addAttribute("language", language);

        return "admin-product";
    }

    @Transactional
    @PutMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, MultipartFile[] files) {
        Product temp = productService.getById(id);

        product.setImages(temp.getImages());

        productService.save(product, files);

        return "redirect:/admin/products/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);

        return "redirect:/admin/products";
    }

    @Transactional
    @GetMapping("/{product_id}/images/{image_id}")
    public String deleteProductImage(@PathVariable Long product_id,
                                     @PathVariable Long image_id) {
        Product product = productService.getById(product_id);

        productService.deleteProductImage(product, image_id);

        imageService.deleteById(image_id);

        return "redirect:/admin/products/" + product_id;
    }
}
