package com.Ecommerce.Ecommerce.model.cart;

import com.Ecommerce.Ecommerce.model.actors.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    @Id
    private String code;

    private double discountPercentage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isActive = true;

    private LocalDateTime expiryDate;
}