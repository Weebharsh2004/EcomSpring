package com.Ecommerce.Ecommerce.dataTransfer.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponseDTO {
    private String code;
    private double discountPercentage;
    private String userEmail;
    private LocalDateTime expiryDate;
    private boolean isActive;
}
