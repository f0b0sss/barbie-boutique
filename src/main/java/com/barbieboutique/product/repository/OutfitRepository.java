package com.barbieboutique.product.repository;


import com.barbieboutique.product.entity.Outfit;
import com.barbieboutique.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OutfitRepository extends JpaRepository<Outfit, Long>{

    List<Outfit> findAllByProductsContaining(Product product);

    //for searching API
    @Query(value = "SELECT min(price) FROM Outfit")
    BigDecimal minPrice();

    @Query(value = "SELECT max(price) FROM Outfit")
    BigDecimal maxPrice();

    List<Outfit> findByPriceBetween(BigDecimal min, BigDecimal max);

    @Query("from Outfit o join o.outfitTitles t where lower(VALUE(t)) like %?1%")
    List<Outfit> findByKeyword(String keyword);

}
