package com.barbieboutique.product.controller;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.category.service.CategoryService;
import com.barbieboutique.filter.entity.Filter;
import com.barbieboutique.filter.service.FilterService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.service.ProductService;
import com.barbieboutique.searchFilterAPI.PriceRange;
import com.barbieboutique.searchFilterAPI.SearchEntityDTO;
import com.barbieboutique.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FilterService filterService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private Utils utils;

    @GetMapping
    public String products(Model model,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {
        Language language = utils.getCurrentLanguage();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        List<Filter> filters = filterService.getALL();
        Page<Product> productPage = productService.findAll(PageRequest.of(currentPage - 1, pageSize));
        List<Category> categories = categoryService.getALL();

        BigDecimal minPrice = productService.minPrice();
        BigDecimal maxPrice = productService.maxPrice();
        PriceRange priceRange = new PriceRange(minPrice, maxPrice);

        SearchEntityDTO searchEntityDTO = new SearchEntityDTO();
        searchEntityDTO.setPriceRange(priceRange);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("language", language);
        model.addAttribute("categories", categories);
        model.addAttribute("filters", filters);
        model.addAttribute("searchEntityDTO", searchEntityDTO);

        return "products";
    }


    @GetMapping("/search")
    public String search(@ModelAttribute SearchEntityDTO searchEntityDTO, Model model,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        List<Product> products = productService.findByPriceBetween(
                searchEntityDTO.getPriceRange().getMin(),
                searchEntityDTO.getPriceRange().getMax());

        if (searchEntityDTO.getTitle() != null) {
            List<Product> temp = productService.findByKeyword(searchEntityDTO.getTitle().toLowerCase());
            products = products.stream()
                    .filter(temp::contains)
                    .collect(Collectors.toList());
        }
        if (searchEntityDTO.getCategories().size() != 0) {
            List<Product> temp = productService.findAllByCategoriesIn(searchEntityDTO.getCategories());
            products = products.stream()
                    .filter(temp::contains)
                    .collect(Collectors.toList());
        }
        if (searchEntityDTO.getAttributes().size() != 0) {
            List<Product> temp = productService.findAllByAttributesIn(searchEntityDTO.getAttributes());
            products = products.stream()
                    .filter(temp::contains)
                    .collect(Collectors.toList());
        }

        Language language = utils.getCurrentLanguage();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(8);

        List<Category> categories = categoryService.getALL();
        List<Filter> filters = filterService.getALL();
        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(currentPage - 1, pageSize), products.size());


        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("language", language);
        model.addAttribute("categories", categories);
        model.addAttribute("filters", filters);
        model.addAttribute("searchEntityDTO", searchEntityDTO);

        return "products";
    }

    @Transactional
    @GetMapping("/{id}")
    public String product(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        Language language = utils.getCurrentLanguage();

        List<Category> categories = product.getCategories().stream().toList();
        List<Filter> filters = filterService.getALL();
        List<Product> outfits = productService.findAllByProductsContaining(product);

        model.addAttribute("product", product);
        model.addAttribute("outfits", outfits);
        model.addAttribute("categories", categories);
        model.addAttribute("filters", filters);
        model.addAttribute("images",  product.getImages());
        model.addAttribute("language", language);

        return "product";
    }

    @GetMapping("{id}/card")
    public String addToCard(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        productService.addToUserCard(id, principal.getName());

        return "redirect:/products/" + id;
    }

}
