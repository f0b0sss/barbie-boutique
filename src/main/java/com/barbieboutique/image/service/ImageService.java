package com.barbieboutique.image.service;

import com.barbieboutique.image.entity.Image;

public interface ImageService {

    Image getById(Long id);

    void deleteById(Long id);


}
