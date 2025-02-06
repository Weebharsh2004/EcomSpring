package com.Ecommerce.Ecommerce.dataTransfer.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCouponResponseDTO {
    private String code;
    private double discountPercentage;
    private String userEmail;
    private boolean isActive;
    private LocalDateTime expiryDate;
}
