package com.Ecommerce.Ecommerce.dataTransfer.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCouponDTO {
    @NotBlank
    private String code;

    @Positive
    private double discountPercentage;

    private String userEmail;
    private boolean isActive=true;
    private LocalDateTime expiryDate;
}
