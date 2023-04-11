package com.barbieboutique.searchFilterAPI;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceRange {
    private BigDecimal min;
    private BigDecimal max;

    public PriceRange() {
    }

    public PriceRange(BigDecimal min, BigDecimal max) {
        this.min = min;
        this.max = max;
    }
}
