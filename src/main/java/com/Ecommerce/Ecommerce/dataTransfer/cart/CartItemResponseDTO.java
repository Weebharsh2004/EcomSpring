package com.Ecommerce.Ecommerce.dataTransfer.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
}
