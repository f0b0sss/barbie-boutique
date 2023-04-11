package com.barbieboutique.product.controller;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.category.service.CategoryService;
import com.barbieboutique.filter.entity.Filter;
import com.barbieboutique.filter.service.FilterService;
import com.barbieboutique.image.entity.Image;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.product.entity.Outfit;
import com.barbieboutique.product.service.OutfitService;
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
@RequestMapping("/outfits")
public class OutfitController {
    @Autowired
    private OutfitService outfitService;
    @Autowired
    private FilterService filterService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private Utils utils;

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

        return "outfits";
    }



    @GetMapping("/search")
    public String search(@ModelAttribute SearchEntityDTO searchEntityDTO, Model model,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        List<Outfit> outfits = outfitService.findByPriceBetween(
                searchEntityDTO.getPriceRange().getMin(),
                searchEntityDTO.getPriceRange().getMax());

        if (searchEntityDTO.getTitle() != null) {
            List<Outfit> temp = outfitService.findByKeyword(searchEntityDTO.getTitle().toLowerCase());
            outfits = outfits.stream()
                    .filter(temp::contains)
                    .collect(Collectors.toList());
        }

        Language language = utils.getCurrentLanguage();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        List<Category> categories = categoryService.getALL();
        List<Filter> filters = filterService.getALL();
        Page<Outfit> outfitPage = new PageImpl<>(outfits, PageRequest.of(currentPage - 1, pageSize), outfits.size());


        int totalPages = outfitPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("outfitPage", outfitPage);
        model.addAttribute("language", language);
        model.addAttribute("categories", categories);
        model.addAttribute("filters", filters);
        model.addAttribute("searchEntityDTO", searchEntityDTO);

        return "outfits";
    }

    @Transactional
    @GetMapping("/{id}")
    public String outfit(@PathVariable Long id, Model model) {
        Outfit outfit = outfitService.getById(id);
        Language language = utils.getCurrentLanguage();

        List<Filter> filters = filterService.getALL();

        List<Image> images = outfit.getImages().stream().toList();

        model.addAttribute("outfit", outfit);
        model.addAttribute("filters", filters);
        model.addAttribute("images", images);
        model.addAttribute("language", language);

        return "outfit";
    }

    @GetMapping("{id}/card")
    public String addToCard(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        outfitService.addToUserCard(id, principal.getName());

        return "redirect:/outfits/" + id;
    }

}
