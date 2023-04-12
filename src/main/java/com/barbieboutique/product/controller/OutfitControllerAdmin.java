package com.barbieboutique.product.controller;


import com.barbieboutique.category.service.CategoryService;
import com.barbieboutique.filter.service.FilterService;
import com.barbieboutique.image.entity.Image;
import com.barbieboutique.image.service.ImageService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.product.entity.Outfit;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.service.OutfitService;
import com.barbieboutique.product.service.ProductService;
import com.barbieboutique.searchFilterAPI.PriceRange;
import com.barbieboutique.searchFilterAPI.SearchEntityDTO;
import com.barbieboutique.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/admin/outfits")
public class OutfitControllerAdmin {
    @Autowired
    private OutfitService outfitService;
    @Autowired
    private ProductService productService;
    @Autowired
    private Utils utils;
    @Autowired
    private FilterService filterService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ImageService imageService;


    @GetMapping
    public String outfits(Model model,
                          @RequestParam(value = "page") Optional<Integer> page,
                          @RequestParam(value = "size") Optional<Integer> size) {
        Language language = utils.getCurrentLanguage();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Outfit> outfitPage = outfitService.findAll(PageRequest.of(currentPage - 1, pageSize));

        BigDecimal minPrice = outfitService.minPrice();
        BigDecimal maxPrice = outfitService.maxPrice();
        PriceRange priceRange = new PriceRange(minPrice, maxPrice);

        SearchEntityDTO searchEntityDTO = new SearchEntityDTO();
        searchEntityDTO.setPriceRange(priceRange);

        int totalPages = outfitPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("outfitPage", outfitPage);
        model.addAttribute("language", language);
        model.addAttribute("searchEntityDTO", searchEntityDTO);

        return "admin-outfits";
    }

    @GetMapping("/new-outfit")
    public String newOutfit(Model model) {
        Outfit outfit = new Outfit();
        outfit.setOutfitTitles(utils.translatorTemplate());

        List<Product> products = productService.findAll();
        Language language = utils.getCurrentLanguage();

        model.addAttribute("outfit", outfit);
        model.addAttribute("products", products);
        model.addAttribute("language", language);

        return "new-outfit";
    }

    @PostMapping
    public String addOutfit(@ModelAttribute Outfit outfit, MultipartFile[] files) {
        outfitService.save(outfit, files);

        return "redirect:/admin/outfits";
    }

    @Transactional
    @GetMapping("/{id}")
    public String editOutfit(@PathVariable Long id, Model model) {
        Outfit outfit = outfitService.getById(id);
        List<Image> images = outfit.getImages().stream().toList();
        Language language = utils.getCurrentLanguage();

        model.addAttribute("outfit", outfit);
        model.addAttribute("images", images);
        model.addAttribute("language", language);

        return "admin-outfit";
    }

    @Transactional
    @PutMapping("/{id}")
    public String updateOutfit(@PathVariable Long id,
                                @ModelAttribute Outfit outfit,
                                MultipartFile[] files) {
        Outfit temp = outfitService.getById(id);

        outfit.setImages(temp.getImages());
        outfit.setProducts(temp.getProducts());

        outfitService.save(outfit, files);

        return "redirect:/admin/outfits/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteOutfit(@PathVariable Long id) {
        outfitService.deleteById(id);

        return "redirect:/admin/outfits";
    }

    @Transactional
    @GetMapping("/{outfit_id}/images/{image_id}")
    public String deleteOutfitImage(@PathVariable Long outfit_id,
                                     @PathVariable Long image_id) {
        Outfit outfit = outfitService.getById(outfit_id);

        outfitService.deleteOutfitImage(outfit, image_id);

        imageService.deleteById(image_id);

        return "redirect:/admin/outfits/" + outfit_id;
    }
}
