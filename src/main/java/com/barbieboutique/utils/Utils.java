package com.barbieboutique.utils;


import com.barbieboutique.image.entity.Image;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.language.service.LanguageService;
import lombok.AllArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@AllArgsConstructor
public class Utils {
    private final LanguageService languageService;

    public Map<Language, String> translatorTemplate(){
        List<Language> languages = languageService.getALL();

        Map<Language, String> temp = new HashMap<>(3);

        for (Language language : languages) {
            temp.put(language, null);
        }

        return temp;
    }

    public List<Image> getImagesFromMultipart(MultipartFile[] files){
        List<Image> images = new ArrayList<>();

        List<MultipartFile> tempList = Arrays.asList(files);

        tempList.stream().forEach(file -> {
            Image image = convertMultipartToImage(file);
            images.add(image);
        });

        return images;
    }

    public Image getImagesFromMultipart(MultipartFile file){
        return convertMultipartToImage(file);
    }


    private Image convertMultipartToImage(MultipartFile file) {
        Image image = null;

//                try {
//            image = Image.builder()
//                    .name(file.getName())
//                    .originalFileName(file.getOriginalFilename())
//                    .contentType(file.getContentType())
//                    .size(file.getSize())
//                    .image(Base64.getEncoder().encodeToString(file.getBytes()))
////                    .bytes(file.getBytes())
//                    .build();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            image = Image.builder()
                    .name(file.getName())
                    .originalFileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .bytes(file.getBytes())
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public Language getCurrentLanguage(){
        Locale locale = LocaleContextHolder.getLocale();
        Language language = languageService.getByCode(locale.getLanguage());

        return language;
    }
}
