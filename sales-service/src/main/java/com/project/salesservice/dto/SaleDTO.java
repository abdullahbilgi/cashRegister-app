package com.project.salesservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
public class SaleDTO {

    private Long id;

    @NotBlank(message = "Ticket code is mandatory")
    @Size(min = 5, max = 20, message = "Sale code must be between 5 and 20 characters")
    private String code;

    @NotNull(message = "Ticket count is mandatory")
    @Min(value = 1, message = "Ticket count must be at least 1")
    private Integer count;

    @NotNull(message = "Total price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    private Double totalPrice;

    @NotNull(message = "User ID is mandatory")
    private Long userId;

    @NotNull(message = "Product ID is mandatory")
    private Long eventId;

    private boolean isSuspended;

    @NotNull(message = "Sale date is mandatory")
    @Future(message = "Sale date must be in the future")
    private Date saleDate;

}
