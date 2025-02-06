package com.Ecommerce.Ecommerce.model.cart;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Getter
    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;


}