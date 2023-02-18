package com.barbieboutique.card.service;


import com.barbieboutique.card.dto.BucketDTO;
import com.barbieboutique.card.entity.Bucket;
import com.barbieboutique.user.entity.User;

import java.util.List;

public interface BucketService {
    Bucket createBucket(User user);

    void addProducts(Bucket bucket, List<Long> productsId);

    BucketDTO getBucketByUser(String email);

    void deleteProductFromBucket(Long product_id, Long bucket_id);

//    void deleteByProductsIsIn(Product product);
}
