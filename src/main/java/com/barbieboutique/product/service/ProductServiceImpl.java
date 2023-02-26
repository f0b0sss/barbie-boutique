package com.barbieboutique.product.service;


import com.barbieboutique.card.entity.Bucket;
import com.barbieboutique.card.service.BucketService;
import com.barbieboutique.category.entity.Category;
import com.barbieboutique.image.entity.Image;
import com.barbieboutique.product.dto.mapper.ProductMapper;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.repository.ProductRepository;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import com.barbieboutique.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper = ProductMapper.MAPPER;

    private final ProductRepository productRepository;
    private final Utils utils;

    private final UserService userService;
    private final BucketService bucketService;


    @Override
    public List<Product> getALL() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllByCategoriesIn(List<Category> categories) {
        return productRepository.findAllByCategoriesIn(categories);
    }

    @Override
    public List<Product> findAllByCategories(Category category) {
        return productRepository.findAllByCategories(category);
    }

    @Override
    @Transactional
    public void addToUserCard(Long productId, String email) {
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found by email - " + email);
        }

        Bucket bucket = user.getBucket();

        bucketService.addProducts(bucket, Collections.singletonList(productId));

//        if (bucket == null) {
//            Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
//            user.setBucket(newBucket);
//            userService.save(user);
//        } else {
//            bucketService.addProducts(bucket, Collections.singletonList(productId));
//        }
    }


    @Override
    public void save(Product product, MultipartFile[] files) {
        List<Image> newImages = null;

        if (!files[0].isEmpty()) {
            newImages = utils.getImagesFromMultipart(files);
        }

        if (product.getId() == null) {
            product.setImages(newImages);
            product.setPreviewImage(newImages.get(0));
        } else {
            if (newImages != null) {
                product.getImages().addAll(newImages);
            }
        }

        productRepository.save(product);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void deleteProductImage(Product product, Long image_id) {

        if (product.getPreviewImage().getId() == image_id) {
            int indexRemoveImage = product.getImages().indexOf(product.getPreviewImage());
            if (indexRemoveImage != product.getImages().size() - 1) {
                product.setPreviewImage(product.getImages().get(indexRemoveImage + 1));
            } else {
                product.setPreviewImage(product.getImages().get(0));
            }
        }

        List<Image> images = product.getImages().stream()
                .filter(image -> image.getId() != image_id)
                .collect(Collectors.toList());

        product.setImages(images);

        productRepository.save(product);
    }
}
