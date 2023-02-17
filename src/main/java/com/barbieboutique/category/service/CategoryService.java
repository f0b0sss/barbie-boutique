package com.barbieboutique.category.service;


import com.barbieboutique.category.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    List<Category> getALL();

    List<Category> findAllByParentCategoryId(Long id);
//    List<Category> findAllByParentCategory(Long id);

    void save(Category category, MultipartFile file);

    Category getById(Long id);

    void deleteById(Long id);

}
