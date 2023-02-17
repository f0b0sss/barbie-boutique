package com.barbieboutique.product.controller;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.filter.entity.Filter;
import com.barbieboutique.filter.service.FilterService;
import com.barbieboutique.image.entity.Image;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.service.ProductService;
import com.barbieboutique.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final FilterService filterService;
    private final Utils utils;

    @GetMapping
    public String products(Model model){
        List<Product> products = productService.getALL();

        Language language = utils.getCurrentLanguage();

        model.addAttribute("products", products);
        model.addAttribute("language", language);

        return "products";
    }

    @Transactional
    @GetMapping("/{id}")
    public String product(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        Language language = utils.getCurrentLanguage();

        List<Category> categories = product.getCategories().stream().toList();
        List<Filter> filters = filterService.getALL();

        List<Image> images = product.getImages().stream().toList();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("filters", filters);
        model.addAttribute("images", images);
        model.addAttribute("language", language);

        return "product";
    }

//    @GetMapping
//    public ProductDTO sayHEllo(@PathVariable Long id){
//        Product product = productService.getById(id);
//        return ProductMapper.MAPPER.toProductDTO(product);
//    }

    @GetMapping("{id}/card")
    public String addToCard(@PathVariable Long id, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        productService.addToUserCard(id, principal.getName());
        return "redirect:/products/" + id;
    }



//
//    public ResponseEntity<Void> addProduct(ProductDTO productDTO, List<MultipartFile> images){
//        System.out.println("title - " + productDTO.getTitle());
//        productService.addProduct(productDTO, images);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

//    @MessageMapping("/products")
//    public void messageProduct(ProductDTO productDTO, List<MultipartFile> images){
//        productService.addProduct(productDTO, images);
//    }

}
