package com.Ecommerce.Ecommerce.service;

import com.Ecommerce.Ecommerce.dataTransfer.coupon.CouponDTO;
import com.Ecommerce.Ecommerce.dataTransfer.coupon.CouponResponseDTO;
import com.Ecommerce.Ecommerce.model.actors.User;
import com.Ecommerce.Ecommerce.model.cart.Coupon;
import com.Ecommerce.Ecommerce.repository.coupon.CouponRepository;
import com.Ecommerce.Ecommerce.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    @Transactional
    public CouponResponseDTO createCoupon(CouponDTO couponDTO) {
        if (couponRepository.existsById(couponDTO.getCode())) {
            throw new ResponseStatusException(BAD_REQUEST, "Coupon code already exists");
        }

        User user = null;
        if (couponDTO.getUserEmail() != null) {
            user = userRepository.findByEmail(couponDTO.getUserEmail())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
        }

        Coupon coupon = Coupon.builder()
                .code(couponDTO.getCode())
                .discountPercentage(couponDTO.getDiscountPercentage())
                .user(user)
                .isActive(couponDTO.getIsActive() != null ? couponDTO.getIsActive() : true)
                .expiryDate(couponDTO.getExpiryDate() != null ? couponDTO.getExpiryDate() : LocalDateTime.now().plusMonths(1))
                .build();

        couponRepository.save(coupon);

        return new CouponResponseDTO(
                coupon.getCode(),
                coupon.getDiscountPercentage(),
                coupon.getUser() != null ? coupon.getUser().getEmail() : null,
                coupon.getExpiryDate(),
                coupon.isActive()
        );
    }

    @Transactional
    public void deleteCoupon(String code) {
        if (!couponRepository.existsById(code)) {
            throw new ResponseStatusException(NOT_FOUND, "Coupon not found");
        }
        couponRepository.deleteById(code);
    }

    public List<CouponResponseDTO> getAllCoupons() {
        return couponRepository.findAll().stream().map(coupon ->
                new CouponResponseDTO(
                        coupon.getCode(),
                        coupon.getDiscountPercentage(),
                        coupon.getUser() != null ? coupon.getUser().getEmail() : null,
                        coupon.getExpiryDate(),
                        coupon.isActive()
                )).collect(Collectors.toList());
    }
}