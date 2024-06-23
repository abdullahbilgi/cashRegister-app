package com.project.salesservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleFilterDTO {

    private Long minCount;
    private Long maxCount;
    private Double minTotalPrice;
    private Double maxTotalPrice;
    private Long productId;
    private Long userId;
    private Integer page = 1;
    private Integer size = 10;
}
