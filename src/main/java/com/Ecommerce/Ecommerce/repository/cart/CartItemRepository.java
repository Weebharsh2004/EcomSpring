package com.Ecommerce.Ecommerce.repository.cart;

import com.Ecommerce.Ecommerce.model.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);

    @Modifying
    @Query("UPDATE CartItem ci SET ci.quantity = :quantity WHERE ci.id = :id")
    void updateQuantity(@Param("id") Long id, @Param("quantity") int quantity);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
