package com.barbieboutique.image.repository;


import com.barbieboutique.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(enableDefaultTransactions = false)
public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteById(Long id);

}
