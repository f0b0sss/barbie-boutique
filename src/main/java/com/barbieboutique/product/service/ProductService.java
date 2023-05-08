package com.barbieboutique.product.service;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.filter.entity.Attribute;
import com.barbieboutique.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<Product> findAll();

    List<Product> findAll(Specification<Product> specification);

    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

    Page<Product> findAll(Pageable pageable);

    List<Product> findAllByProductsContaining(Product product);

    List<Product> findAllByCategoriesIn(List<Category> categories);

    List<Product> findAllByCategories(Category category);

    void addToUserCard(Long productId, String username);

    void save(Product product, MultipartFile[] files);

    Product getById(Long id);

    BigDecimal getPrice(Long id);

    void deleteById(Long id);

    void deleteProductImage(Product product, Long image_id);


    //    search API
    BigDecimal minPrice();

    BigDecimal maxPrice();
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
    List<Product> findByKeyword(String keyword);

    List<Product> findAllByAttributesIn(List<Attribute> attributes);


}
