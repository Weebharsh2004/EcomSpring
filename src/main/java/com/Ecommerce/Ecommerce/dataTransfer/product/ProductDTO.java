package com.Ecommerce.Ecommerce.dataTransfer.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @Positive
    private double price;

    @PositiveOrZero
    private int stockQuantity;

    public interface Create {}
    public interface Update {}
}