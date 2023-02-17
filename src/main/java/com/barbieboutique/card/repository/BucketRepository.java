package com.barbieboutique.card.repository;

import com.barbieboutique.card.entity.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
//    void deleteByProductsIsIn(Product product);
}
