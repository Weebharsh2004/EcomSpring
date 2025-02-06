package com.Ecommerce.Ecommerce.dataTransfer.admin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RestockRequestDTO {
    @NotNull
    private Long productId;

    @Positive
    private int quantity;
}
