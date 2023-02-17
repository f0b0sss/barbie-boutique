package com.barbieboutique.filter.controller;


import com.barbieboutique.filter.entity.Attribute;
import com.barbieboutique.filter.entity.Filter;
import com.barbieboutique.filter.service.AttributeService;
import com.barbieboutique.filter.service.FilterService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.language.service.LanguageService;
import com.barbieboutique.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/filters")
public class FilterControllerAdmin {
    private final FilterService filterService;
    private final AttributeService attributeService;
    private final Utils utils;
    private final LanguageService languageService;

    @GetMapping
    public String filters(Model model) {
        List<Filter> filters = filterService.getALL();
        Language language = languageService.getByCode("ru");

        model.addAttribute("filters", filters);
        model.addAttribute("language", language);

        return "admin-filters";
    }

    @GetMapping("/new-filter")
    public String newFilter(Model model) {
        Filter filter = new Filter();

        filter.setFilterTitles(utils.translatorTemplate());

        model.addAttribute("filter", filter);

        return "new-filter";
    }

    @PostMapping
    public String addFilter(@ModelAttribute Filter filter) {
        filterService.save(filter);

        return "redirect:/admin/filters";
    }

    @GetMapping("/{id}")
    public String editFilter(@PathVariable Long id, Model model) {
        Filter filter = filterService.getById(id);
        Language language = languageService.getByCode("ru");
        Attribute newAttribute = new Attribute();

        newAttribute.setAttributeTitles(utils.translatorTemplate());

        model.addAttribute("filter", filter);
        model.addAttribute("language", language);
        model.addAttribute("newAttribute", newAttribute);

        return "admin-filter";
    }

    @PutMapping("/{id}")
    public String updateFilter(@ModelAttribute Filter filter) {
        filterService.save(filter);

        return "redirect:/admin/filters";
    }

    @DeleteMapping("/{id}")
    public String deleteFilter(@PathVariable Long id) {
        filterService.deleteById(id);

        return "redirect:/admin/filters";
    }
}
