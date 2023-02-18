package com.barbieboutique.language.service;

import com.barbieboutique.language.entity.Language;
import com.barbieboutique.language.repository.LanguageRepository;
import com.barbieboutique.user.dao.UserRepository;
import com.barbieboutique.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class LanguageServiceImplTest {

    private LanguageService languageService;

    @Mock
    private LanguageRepository languageRepository;
    private Language language;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.languageService = new LanguageServiceImpl(languageRepository);

        this.language = new Language(1l, "en", "English");
    }

    @Test
    void getALL() {
    }

    @Test
    void add() {
    }

    @Test
    void getById() {
        given(languageRepository.findById(1l)).willReturn(Optional.ofNullable(this.language));

        Language  expected = languageService.getById(1l);

        assertEquals(expected, this.language);
        verify(languageRepository).findById(1l);
    }

    @Test
    void getByCode() {
    }

    @Test
    void deleteById() {
    }
}