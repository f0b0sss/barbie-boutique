package com.barbieboutique.bucket.controller;


import com.barbieboutique.bucket.dto.BucketDTO;
import com.barbieboutique.bucket.service.BucketService;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import com.barbieboutique.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/bucket")
@AllArgsConstructor
public class BucketController {
    private final BucketService bucketService;
    private final UserService userService;
    private final Utils utils;


    @Transactional
    @GetMapping
    public String bucket(Model model, Principal principal) {
        if (principal == null) {
            return "login";
        } else {
            BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());

            model.addAttribute("bucketDTO", bucketDTO);
        }

        Language language = utils.getCurrentLanguage();

        model.addAttribute("language", language);

        return "bucket";
    }

    @DeleteMapping("/{id}")
    public String deleteProductFromBucket(@PathVariable Long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Long bucket_id = user.getBucket().getId();

        bucketService.deleteProductFromBucket(id, bucket_id);

        return "redirect:/bucket";
    }
}
