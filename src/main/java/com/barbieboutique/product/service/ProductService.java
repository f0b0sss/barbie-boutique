package com.barbieboutique.product.service;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> getALL();

    List<Product> findAllByCategoriesIn(List<Category> categories);

    List<Product> findAllByCategories(Category category);

    void addToUserCard(Long productId, String username);

    void save(Product product, MultipartFile[] files);

    Product getById(Long id);

    void deleteById(Long id);

    void deleteProductImage(Product product, Long image_id);
}
