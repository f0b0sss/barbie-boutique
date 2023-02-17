package com.barbieboutique.product.repository;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoriesIn(List<Category> categories);

    List<Product> findAllByCategories(Category category);




}
