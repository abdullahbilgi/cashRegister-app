package com.project.productservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "_product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer code;
    private Double price;

    @Lob
    @Column(length = 100000)
    private String description;

    private Long capacity;

    @Temporal(TemporalType.DATE)
    private Date productDate;




}
