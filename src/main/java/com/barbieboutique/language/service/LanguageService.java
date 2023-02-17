package com.barbieboutique.language.service;


import com.barbieboutique.language.entity.Language;

import java.util.List;

public interface LanguageService {
    List<Language> getALL();

    void add(Language language);

    Language getById(Long id);

    Language getByCode(String code);

    void deleteById(Long id);
}
