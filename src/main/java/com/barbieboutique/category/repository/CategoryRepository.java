package com.barbieboutique.category.repository;

import com.barbieboutique.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    void deleteById(Long id);

    List<Category> findAllByParentCategoryId(Long id);

}
