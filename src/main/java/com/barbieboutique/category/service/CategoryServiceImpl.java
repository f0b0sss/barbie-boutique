package com.barbieboutique.category.service;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.category.repository.CategoryRepository;
import com.barbieboutique.image.entity.Image;
import com.barbieboutique.image.service.ImageService;
import com.barbieboutique.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final Utils utils;

    @Override
    public List<Category> getALL() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findAllByParentCategoryId(Long id) {
        return categoryRepository.findAllByParentCategoryId(id);
    }

    @Transactional
    @Override
    public void save(Category category, MultipartFile file) {
        Image previousImage = null;
        Image newImage = new Image();


        if (category.getId() == null){
            System.out.println("i1");
            if (!file.isEmpty()){
                newImage = utils.getImagesFromMultipart(file);
            }
            category.setImage(newImage);
        }else {
            previousImage = getById(category.getId()).getImage();

            if (file.isEmpty()){
                category.setImage(previousImage);
            }else {
                newImage = utils.getImagesFromMultipart(file);
                
                category.setImage(newImage);
            }
        }

        categoryRepository.save(category);

        if (!file.isEmpty() && category.getId() != null && previousImage.getId() != null){
            imageService.deleteById(previousImage.getId());
        }
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        List<Category> categories = categoryRepository.findAll();

        categories.stream()
                        .forEach(category -> {
                            if (category.getParentCategory() != null &&
                                    category.getParentCategory().getId() == id){
                                category.setParentCategory(null);
                            }
                        });

        categoryRepository.saveAll(categories);

        categoryRepository.deleteById(id);
    }

}
