package com.barbieboutique.image.controller;


import com.barbieboutique.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ImageControllerAdmin {
    private final ImageService imageService;

//    @DeleteMapping("/images/{id}")
//    public String deleteImage(@PathVariable Long id) {
//        imageService.deleteById(id);
//
//        return "redirect:/admin/categories";
//    }

}
