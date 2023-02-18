package com.barbieboutique.card.service;


import com.barbieboutique.card.dto.BucketDTO;
import com.barbieboutique.card.dto.BucketDetailDTO;
import com.barbieboutique.card.entity.Bucket;
import com.barbieboutique.card.repository.BucketRepository;
import com.barbieboutique.product.entity.Product;
import com.barbieboutique.product.repository.ProductRepository;
import com.barbieboutique.user.entity.User;
import com.barbieboutique.user.service.UserService;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BucketServiceImpl implements BucketService{
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserService userService;



//    @Override
//    @Transactional
//    public Bucket createBucket(User user, List<Long> productsId) {
//        Bucket bucket = new Bucket();
//        bucket.setUser(user);
//        List<Product> productList = getCollectRefProductsById(productsId);
//        bucket.setProducts(productList);
//        return bucketRepository.save(bucket);
//    }

    @Override
    @Transactional
    public Bucket createBucket(User user) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List<Product> productList = new ArrayList<>();
        bucket.setProducts(productList);

        return bucketRepository.save(bucket);
    }

    private List<Product> getCollectRefProductsById(List<Long> productsId) {
        return productsId.stream()
                .map(productRepository::getReferenceById)
                .collect(Collectors.toList());
    }

    @Override
    public void addProducts(Bucket bucket, List<Long> productsId) {
        List<Product> products = bucket.getProducts();

        List<Product> newProductsList = products == null ? new ArrayList<>() : new ArrayList<>(products);

        newProductsList.addAll(getCollectRefProductsById(productsId));
        bucket.setProducts(newProductsList);

        bucketRepository.save(bucket);
    }

    @Override
    public BucketDTO getBucketByUser(String email) {
        User user = userService.findByEmail(email);

        BucketDTO bucketDTO = new BucketDTO();

        if (user.getBucket() == null){
            createBucket(user);
        }

        Map<Long, BucketDetailDTO> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();

        for (Product product : products) {
            BucketDetailDTO detailDTO = mapByProductId.get(product.getId());

            if (detailDTO == null){
                mapByProductId.put(product.getId(), new BucketDetailDTO(product));
            }else {
                detailDTO.setAmount(detailDTO.getAmount().add(new BigDecimal(1.0)));
                detailDTO.setSum(detailDTO.getSum() + Double.valueOf(product.getPrice().toString()));
            }
        }

        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();

        return bucketDTO;
    }

    @Transactional
    @Override
    public void deleteProductFromBucket(Long product_id, Long bucket_id) {
        Bucket bucket = bucketRepository.findById(bucket_id).get();

        Product product = productRepository.findById(product_id).get();

        bucket.getProducts().remove(product);

        bucketRepository.save(bucket);
    }

//    @Override
//    public void deleteByProductsIsIn(Product product) {
//        bucketRepository.deleteByProductsIsIn(product);
//    }
}
