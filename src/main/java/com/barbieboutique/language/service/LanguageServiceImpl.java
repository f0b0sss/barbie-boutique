package com.barbieboutique.language.service;


import com.barbieboutique.language.entity.Language;
import com.barbieboutique.language.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LanguageServiceImpl implements LanguageService{
    private final LanguageRepository languageRepository;


    @Override
    public List<Language> getALL() {
        return languageRepository.findAll();
    }

    @Override
    public void add(Language language) {
        languageRepository.save(language);
    }

    @Override
    public Language getById(Long id) {
        return languageRepository.findById(id).orElseThrow();
    }

    @Override
    public Language getByCode(String code) {
        return languageRepository.findByCode(code);
    }

    @Override
    public void deleteById(Long id) {
        languageRepository.deleteById(id);
    }
}
