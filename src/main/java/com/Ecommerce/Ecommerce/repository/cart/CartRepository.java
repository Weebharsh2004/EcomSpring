package com.Ecommerce.Ecommerce.repository.cart;

import com.Ecommerce.Ecommerce.model.actors.User;
import com.Ecommerce.Ecommerce.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart>findByUser(User user);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Cart c JOIN FETCH c.items WHERE c.user.id = :userId")
    Optional<Cart> findByUserIdWithItems(@Param("userId") Long userId);
}
