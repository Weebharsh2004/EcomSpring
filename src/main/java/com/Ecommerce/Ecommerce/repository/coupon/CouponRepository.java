package com.Ecommerce.Ecommerce.repository.coupon;

import com.Ecommerce.Ecommerce.model.actors.User;
import com.Ecommerce.Ecommerce.model.cart.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, String > {
    @Query("SELECT c FROM Coupon c WHERE (c.user IS NULL OR c.user = :user) " +
            "AND c.isActive = true AND (c.expiryDate IS NULL OR c.expiryDate > CURRENT_TIMESTAMP)")
    List<Coupon> findValidCouponsForUser(@Param("user") User user);

    List<Coupon> findByUser(User user);
    List<Coupon>findByUserIsNull();

    @Modifying
    @Query("UPDATE Coupon c SET c.isActive = false WHERE c.expiryDate < CURRENT_TIMESTAMP")
    void deactivateExpiredCoupons();
}
