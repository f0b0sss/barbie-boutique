package com.barbieboutique.product.service;


import com.barbieboutique.product.entity.Outfit;
import com.barbieboutique.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface OutfitService {
    Page<Outfit> findAll(Pageable pageable);
        List<Outfit> findAllByProductsContaining(Product product);

    void save(Outfit outfit, MultipartFile[] files);

    Outfit getById(Long id);

    void deleteById(Long id);

    void deleteOutfitImage(Outfit outfit, Long image_id);


    //    search API
    BigDecimal minPrice();

    BigDecimal maxPrice();

    List<Outfit> findByPriceBetween(BigDecimal min, BigDecimal max);
    List<Outfit> findByKeyword(String keyword);

    // add to card
    void addToUserCard(Long outfitId, String email);

}
