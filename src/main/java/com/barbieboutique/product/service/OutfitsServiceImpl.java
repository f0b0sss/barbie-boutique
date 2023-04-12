package com.barbieboutique.product.service;


import com.barbieboutique.bucket.entity.Bucket;
import com.barbieboutique.bucket.service.BucketService;
import com.barbieboutique.image.entity.Image;
import com.barbieboutique.product.entity.Outfit;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.repository.OutfitRepository;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import com.barbieboutique.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OutfitsServiceImpl implements OutfitService {

    private final OutfitRepository outfitRepository;
    private final Utils utils;
    private final UserService userService;
    private final BucketService bucketService;

    public Page<Outfit> findAll(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Outfit> outfits = outfitRepository.findAll();

        List<Outfit> list;

        if (outfits.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, outfits.size());
            list = outfits.subList(startItem, toIndex);
        }

        Page<Outfit> outfitsPage = new PageImpl<Outfit>(list, PageRequest.of(currentPage, pageSize), outfits.size());

        return outfitsPage;
    }


    @Override
    public List<Outfit> findAllByProductsContaining(Product product) {
        return outfitRepository.findAllByProductsContaining(product);
    }

    @Override
    public void save(Outfit outfit, MultipartFile[] files) {
        List<Image> newImages = null;

        if (!files[0].isEmpty()) {
            newImages = utils.getImagesFromMultipart(files);
        }

        if (outfit.getId() == null) {
            outfit.setImages(newImages);
            outfit.setPreviewImage(newImages.get(0));
        } else {
            if (newImages != null) {
                outfit.getImages().addAll(newImages);
            }
        }

        BigDecimal price = outfit.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        outfit.setPrice(price);

        outfitRepository.save(outfit);
    }

    @Override
    public Outfit getById(Long id) {
        return outfitRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        outfitRepository.deleteById(id);
    }

    @Override
    public void deleteOutfitImage(Outfit outfit, Long image_id) {
        if (outfit.getPreviewImage().getId() == image_id) {
            int indexRemoveImage = outfit.getImages().indexOf(outfit.getPreviewImage());
            if (indexRemoveImage != outfit.getImages().size() - 1) {
                outfit.setPreviewImage(outfit.getImages().get(indexRemoveImage + 1));
            } else {
                outfit.setPreviewImage(outfit.getImages().get(0));
            }
        }

        List<Image> images = outfit.getImages().stream()
                .filter(image -> image.getId() != image_id)
                .collect(Collectors.toList());

        outfit.setImages(images);

        outfitRepository.save(outfit);
    }

    //    search API
    @Override
    public BigDecimal minPrice() {
        return outfitRepository.minPrice();
    }

    @Override
    public BigDecimal maxPrice() {
        return outfitRepository.maxPrice();
    }

    @Override
    public List<Outfit> findByPriceBetween(BigDecimal min, BigDecimal max) {
        return outfitRepository.findByPriceBetween(min, max);
    }

    @Override
    public List<Outfit> findByKeyword(String keyword) {
        return outfitRepository.findByKeyword(keyword);
    }

    @Override
    @Transactional
    public void addToUserCard(Long outfitId, String email) {
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found by email - " + email);
        }

        Bucket bucket = user.getBucket();

        List<Long> productsIds = getById(outfitId).getProducts().stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());

        bucketService.addProducts(bucket, productsIds);
    }

}
