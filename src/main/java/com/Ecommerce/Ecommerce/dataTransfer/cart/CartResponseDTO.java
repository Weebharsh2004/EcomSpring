package com.Ecommerce.Ecommerce.dataTransfer.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {
    private List<CartItemResponseDTO>items;
    private double subtotal;
    private double discount;
    private double total;
}
