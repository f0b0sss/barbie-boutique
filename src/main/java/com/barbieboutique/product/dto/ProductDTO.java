package com.barbieboutique.product.dto;

import com.barbieboutique.category.entity.Category;
import com.barbieboutique.filter.entity.Attribute;
import com.barbieboutique.language.entity.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private Map<Language, String> productTitles;
    private BigDecimal price;
    private List<Category> categories;
    private List<Attribute> attributes;
}
