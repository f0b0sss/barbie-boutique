package com.barbieboutique.product.service;


import com.barbieboutique.bucket.entity.Bucket;
import com.barbieboutique.bucket.service.BucketService;
import com.barbieboutique.category.entity.Category;
import com.barbieboutique.filter.entity.Attribute;
import com.barbieboutique.image.entity.Image;
import com.barbieboutique.product.dto.mapper.ProductMapper;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.repository.ProductRepository;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import com.barbieboutique.utils.Utils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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

    public Page<Product> findAll(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Product> products = productRepository.findAll();

        List<Product> list;

        if (products.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, products.size());
            list = products.subList(startItem, toIndex);
        }

        Page<Product> productsPage = new PageImpl<Product>(list, PageRequest.of(currentPage, pageSize), products.size());

        return productsPage;
    }

    @Override
    public List<Product> findAllByProductsContaining(Product product) {
        return productRepository.findAllByProductsContaining(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAll(Specification<Product> specification) {
        return productRepository.findAll(specification);
    }

    @Override
    public Page<Product> findAll(Specification<Product> specification, Pageable pageable) {
        return productRepository.findAll(specification, pageable);
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

        BigDecimal currentPrice = getPrice(product.getId());

        if (currentPrice != product.getPrice() && product.getProducts().isEmpty()) {
            BigDecimal diff = currentPrice.subtract(product.getPrice());
            updateParentProductsPrice(product, diff);
        }

        if (!product.getProducts().isEmpty()) {
            totalProductsPrice(product);
        }

        productRepository.save(product);
    }

    private void updateParentProductsPrice(Product product, BigDecimal diff) {
        List<Product> outfits = productRepository.findAllByProductsContaining(product);

        outfits.stream()
                .forEach(o -> o.setPrice(o.getPrice().subtract(diff)));
    }

    private void totalProductsPrice(Product product) {
        BigDecimal price = product.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        product.setPrice(price);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public BigDecimal getPrice(Long id) {
        return productRepository.getPrice(id);
    }

    @Override
    public BigDecimal minPrice() {
        return productRepository.minPrice();
    }

    @Override
    public BigDecimal maxPrice() {
        return productRepository.maxPrice();
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


//    search API

    @Override
    public List<Product> findByPriceBetween(BigDecimal min, BigDecimal max) {
        return productRepository.findByPriceBetween(min, max);
    }

    @Override
    public List<Product> findByKeyword(String keyword) {
        return productRepository.findByKeyword(keyword);
    }

    @Override
    public List<Product> findAllByAttributesIn(List<Attribute> attributes) {
        return productRepository.findAllByAttributesIn(attributes);
    }
}
