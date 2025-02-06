package com.Ecommerce.Ecommerce.model.actors;

import com.Ecommerce.Ecommerce.model.cart.Coupon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN or USER

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Coupon> coupons;

    private boolean enabled;
}

// Enum: Role.java


