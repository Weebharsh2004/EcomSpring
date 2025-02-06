package com.Ecommerce.Ecommerce.dataTransfer.coupon;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {

    @NotBlank(groups = {Admin.Create.class, Admin.Update.class})
    private String code;

    @Positive
    private double discountPercentage;

    // Only admins can set userEmail (for user-specific coupons)
    @Null(groups = User.Create.class) // Users can’t assign coupons
    private String userEmail;

    // Only admins can toggle these fields
    @Null(groups = User.Create.class)
    private Boolean isActive;

    @Null(groups = User.Create.class)
    private LocalDateTime expiryDate;

    // Validation groups
    public interface Admin {
        interface Create {}
        interface Update {}
    }
    public interface User {
        interface Create {}
    }
}