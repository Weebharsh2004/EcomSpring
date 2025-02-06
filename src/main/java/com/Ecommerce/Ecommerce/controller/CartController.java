package com.Ecommerce.Ecommerce.controller;

import com.Ecommerce.Ecommerce.dataTransfer.cart.CartItemDTO;
import com.Ecommerce.Ecommerce.dataTransfer.cart.CartResponseDTO;
import com.Ecommerce.Ecommerce.dataTransfer.checkout.CheckoutRequestDTO;
import com.Ecommerce.Ecommerce.dataTransfer.checkout.CheckoutResponseDTO;
import com.Ecommerce.Ecommerce.security.CustomUserDetails;
import com.Ecommerce.Ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     * Only users with ROLE_USER can add items to the cart.
     * The email is extracted from the token.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody CartItemDTO cartItemDTO) {
        String email = currentUser.getUsername();
        return ResponseEntity.ok(cartService.addToCart(email, cartItemDTO));
    }

    /**
     * Only users with ROLE_USER can remove products from the cart.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponseDTO> removeProduct(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long productId) {
        String email = currentUser.getUsername();
        return ResponseEntity.ok(cartService.removeFromCart(email, productId));
    }

    /**
     * Only users with ROLE_USER can view their cart.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        String email = currentUser.getUsername();
        return ResponseEntity.ok(cartService.getCart(email));
    }

    /**
     * Only users with ROLE_USER can apply discounts to their cart.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/apply-discount")
    public ResponseEntity<CartResponseDTO> applyDiscount(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) String couponCode) {
        String email = currentUser.getUsername();
        return ResponseEntity.ok(cartService.applyDiscountToCart(email, couponCode));
    }

    /**
     * Only users with ROLE_USER can empty their cart.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/empty")
    public ResponseEntity<String> emptyCart(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        String email = currentUser.getUsername();
        cartService.emptyCart(email);
        return ResponseEntity.ok("Cart emptied successfully.");
    }

    /**
     * Only users with ROLE_USER can checkout their cart.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDTO> checkout(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody CheckoutRequestDTO requestDTO) {
        String email = currentUser.getUsername();
        return ResponseEntity.ok(cartService.checkout(email, requestDTO));
    }
}
