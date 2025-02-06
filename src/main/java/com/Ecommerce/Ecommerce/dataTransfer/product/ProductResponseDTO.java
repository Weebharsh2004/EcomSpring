package com.Ecommerce.Ecommerce.dataTransfer.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stockPrice;
}
