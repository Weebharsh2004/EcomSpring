package com.Ecommerce.Ecommerce.dataTransfer.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    @NotNull
    private Long productId;

    @Positive
    private int quantity;
}
