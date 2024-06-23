package com.project.salesservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "sale")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private Integer count;
    private Double totalPrice;
    private Long userId;
    private Long productId;
    private boolean isSuspended;

    @Temporal(TemporalType.DATE)
    private Date saleDate;
}
