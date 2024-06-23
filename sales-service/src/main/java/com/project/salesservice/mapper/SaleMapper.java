package com.project.salesservice.mapper;

import com.project.salesservice.dto.SaleDTO;
import com.project.salesservice.entity.Sale;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper implements ObjectMapper<Sale, SaleDTO>{

    @Override
    public SaleDTO map(Sale sale) {
        return SaleDTO.builder()
                .id(sale.getId())
                .code(sale.getCode())
                .count(sale.getCount())
                .totalPrice(sale.getTotalPrice())
                .userId(sale.getUserId())
                .eventId(sale.getProductId())
                .isSuspended(sale.isSuspended())
                .saleDate(sale.getSaleDate())
                .build();
    }

    @Override
    public Sale reverseMap(SaleDTO saleDTO) {
        return Sale.builder()
                .code(saleDTO.getCode())
                .count(saleDTO.getCount())
                .totalPrice(saleDTO.getTotalPrice())
                .userId(saleDTO.getUserId())
                .productId(saleDTO.getEventId())
                .isSuspended(saleDTO.isSuspended())
                .saleDate(saleDTO.getSaleDate())
                .build();
    }
}
