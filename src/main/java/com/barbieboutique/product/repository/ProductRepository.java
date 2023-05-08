package com.barbieboutique.product.repository;


import com.barbieboutique.category.entity.Category;
import com.barbieboutique.filter.entity.Attribute;
import com.barbieboutique.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findAllByCategoriesIn(List<Category> categories);

    List<Product> findAllByCategories(Category category);

    List<Product> findAllByProductsContaining(Product product);

    @Query(value = "SELECT price FROM Product where id=?1")
    BigDecimal getPrice(Long id);

    //for searching API
    @Query(value = "SELECT min(price) FROM Product")
    BigDecimal minPrice();

    @Query(value = "SELECT max(price) FROM Product")
    BigDecimal maxPrice();

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    @Query("from Product p join p.productTitles t where lower(VALUE(t)) like %?1%")
    List<Product> findByKeyword(String keyword);

    List<Product> findAllByAttributesIn(List<Attribute> attributes);

}
