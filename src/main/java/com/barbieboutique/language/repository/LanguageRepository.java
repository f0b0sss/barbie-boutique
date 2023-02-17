package com.barbieboutique.language.repository;


import com.barbieboutique.language.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Language findByCode(String code);
}
