package com.barbieboutique.searchFilterAPI;

import com.barbieboutique.category.entity.Category;
import com.barbieboutique.filter.entity.Attribute;
import lombok.Data;

import java.util.List;

@Data
public class SearchEntityDTO {
    private String title;
    private List<Category> categories;
    private List<Attribute> attributes;
    private PriceRange priceRange;


    public SearchEntityDTO() {
    }

}
