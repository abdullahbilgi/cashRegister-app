package com.project.productservice.mapper;

import com.project.productservice.dto.ProductDTO;
import com.project.productservice.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements ObjectMapper<Product, ProductDTO>{


    @Override
    public ProductDTO map(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .code(product.getCode())
                .price(product.getPrice())
                .capacity(product.getCapacity())
                .productDate(product.getProductDate())
                .build();
    }

    @Override
    public Product reverseMap(ProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .code(productDTO.getCode())
                .price(productDTO.getPrice())
                .capacity(productDTO.getCapacity())
                .productDate(productDTO.getProductDate())
                .build();
    }
}
