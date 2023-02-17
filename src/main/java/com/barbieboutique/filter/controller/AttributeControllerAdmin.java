package com.barbieboutique.filter.controller;


import com.barbieboutique.filter.entity.Attribute;
import com.barbieboutique.filter.entity.Filter;
import com.barbieboutique.filter.service.AttributeService;
import com.barbieboutique.filter.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/filters")
public class AttributeControllerAdmin {
    private final AttributeService attributeService;
    private final FilterService filterService;

    @GetMapping("/{id}/attributes/{att_id}")
    public String editAttribute(Model model, @PathVariable Long id, @PathVariable Long att_id) {
        Attribute attribute = attributeService.getById(att_id);

        model.addAttribute("attribute", attribute);
        model.addAttribute("filterId", id);

        return "admin-attribute";
    }

    @Transactional
    @PostMapping("/{id}/attributes")
    public String addAttribute(@PathVariable Long id, @ModelAttribute Attribute newAttribute) {
        Filter filter = filterService.getById(id);

        filter.getAttributes().add(newAttribute);

        filterService.save(filter);

        return "redirect:/admin/filters/" + id;
    }

    @PutMapping("/{id}/attributes/{att_id}")
    public String updateAttribute(@ModelAttribute Attribute attribute,
                                  @PathVariable Long id, @PathVariable Long att_id) {
        attribute.setId(att_id);

        attributeService.save(attribute);

        return "redirect:/admin/filters/" + id;
    }

    @Transactional
    @DeleteMapping("/{id}/attributes/{att_id}")
    public String deleteAttribute(@PathVariable Long id, @PathVariable Long att_id) {
        Filter filter = filterService.getById(id);

        List<Attribute> attributes = filter.getAttributes().stream()
                .filter(a -> a.getId() != att_id)
                .collect(Collectors.toList());

        filter.setAttributes(attributes);

        filterService.save(filter);

        attributeService.deleteById(att_id);

        return "redirect:/admin/filters/" + id;
    }
}
