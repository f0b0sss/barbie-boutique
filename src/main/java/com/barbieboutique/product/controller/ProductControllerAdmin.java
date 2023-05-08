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
import com.barbieboutique.searchFilterAPI.PriceRange;
import com.barbieboutique.searchFilterAPI.SearchEntityDTO;
import com.barbieboutique.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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

        return "admin-products";
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

        return "admin-products";
    }

    @GetMapping("/new-product")
    public String newProduct(Model model) {
        Product product = new Product();
        product.setProductTitles(utils.translatorTemplate());
        product.setDescriptions(utils.translatorTemplate());

        List<Category> categories = categoryService.getALL();
        List<Filter> filters = filterService.getALL();
        Language language = utils.getCurrentLanguage();

        List<Product> AllProducts = productService.findAll();
        Collections.sort(AllProducts, Comparator.comparing(Product::getCreatedDate));

        model.addAttribute("AllProducts", AllProducts);
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
    public String product(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        Language language = utils.getCurrentLanguage();
        List<Filter> filters = filterService.getALL();
        List<Category> allCategories = categoryService.getALL();

        List<Product> AllProducts = productService.findAll();
        Collections.sort(AllProducts, Comparator.comparing(Product::getCreatedDate));

        List<Product> outfits = productService.findAllByProductsContaining(product);

        model.addAttribute("outfits", outfits);
        model.addAttribute("product", product);
        model.addAttribute("AllProducts", AllProducts);
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
