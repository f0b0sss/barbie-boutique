package com.barbieboutique.image.service;


import com.barbieboutique.image.entity.Image;
import com.barbieboutique.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public Image getById(Long id) {
        return imageRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        imageRepository.deleteById(id);
    }
}
