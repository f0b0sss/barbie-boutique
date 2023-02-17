package com.barbieboutique.card.dto;

import com.barbieboutique.image.entity.Image;
import com.barbieboutique.language.entity.Language;
import com.barbieboutique.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDetailDTO {
    private Map<Language, String> productTitles;
    private long productId;
    private BigDecimal price;
    private BigDecimal amount;
    private Double sum;
    private Image previewImage;

    public BucketDetailDTO(Product product) {
        this.productTitles = product.getProductTitles();
        this.productId = product.getId();
        this.price = product.getPrice();
        this.amount = new BigDecimal(1.0);
        this.sum = Double.valueOf(product.getPrice().toString());
        this.previewImage = product.getPreviewImage();
    }
}
