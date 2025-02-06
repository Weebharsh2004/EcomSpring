package com.Ecommerce.Ecommerce.dataTransfer.checkout;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CheckoutResponseDTO {
    private double subtotal;
    private double discount;
    private double total;
    private String message;
}
