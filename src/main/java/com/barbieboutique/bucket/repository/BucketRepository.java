package com.barbieboutique.bucket.repository;

import com.barbieboutique.bucket.entity.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
//    void deleteByProductsIsIn(Product product);
}
