package com.barbieboutique.product.dto.mapper;


import com.barbieboutique.product.dto.ProductDTO;
import com.barbieboutique.product.entity.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    Product toProduct(ProductDTO productDTO);

    @InheritInverseConfiguration
    ProductDTO toProductDTO(Product product);

    List<Product> toProductList(List<ProductDTO> productDTOS);

    List<ProductDTO> toProductDTOList(List<Product> products);
}
