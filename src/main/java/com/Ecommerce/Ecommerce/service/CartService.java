package com.Ecommerce.Ecommerce.service;

import com.Ecommerce.Ecommerce.dataTransfer.cart.CartItemDTO;
import com.Ecommerce.Ecommerce.dataTransfer.cart.CartItemResponseDTO;
import com.Ecommerce.Ecommerce.dataTransfer.cart.CartResponseDTO;
import com.Ecommerce.Ecommerce.dataTransfer.checkout.CheckoutRequestDTO;
import com.Ecommerce.Ecommerce.dataTransfer.checkout.CheckoutResponseDTO;
import com.Ecommerce.Ecommerce.model.actors.User;
import com.Ecommerce.Ecommerce.model.cart.Cart;
import com.Ecommerce.Ecommerce.model.cart.CartItem;
import com.Ecommerce.Ecommerce.model.cart.Coupon;
import com.Ecommerce.Ecommerce.model.cart.Product;
import com.Ecommerce.Ecommerce.repository.cart.CartItemRepository;
import com.Ecommerce.Ecommerce.repository.cart.CartRepository;
import com.Ecommerce.Ecommerce.repository.coupon.CouponRepository;
import com.Ecommerce.Ecommerce.repository.product.ProductRepository;
import com.Ecommerce.Ecommerce.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public CartResponseDTO addToCart(String userEmail, CartItemDTO cartItemDTO) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));

        if (cartItemDTO.getQuantity() > 5) {
            throw new ResponseStatusException(BAD_REQUEST, "Cannot add more than 5 items per product");
        }

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + cartItemDTO.getQuantity();
            if (newQuantity > 5) {
                throw new ResponseStatusException(BAD_REQUEST, "Total quantity for this product cannot exceed 5");
            }
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(cartItemDTO.getQuantity());
            cartItemRepository.save(newItem);
        }

        return getCartResponse(cart, 0.0);
    }

    @Transactional
    public CartResponseDTO removeFromCart(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cart not found"));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found in cart"));
        cartItemRepository.delete(cartItem);

        return getCartResponse(cart, 0.0);
    }

    /**
     * Retrieves the cart details without applying any discount.
     */
    public CartResponseDTO getCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
        Cart cart = cartRepository.findByUserIdWithItems(user.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cart not found"));
        return getCartResponse(cart, 0.0);
    }

    /**
     * Applies a discount to the current cart. The discount can come either from a valid coupon or from default discount tiers.
     */
    @Transactional
    public CartResponseDTO applyDiscountToCart(String userEmail, String couponCode) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
        Cart cart = cartRepository.findByUserIdWithItems(user.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cart not found"));
        double subtotal = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        double discount = applyDiscount(user, subtotal, couponCode);
        return getCartResponse(cart, discount);
    }

    @Transactional
    public CartResponseDTO emptyCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cart not found"));
        cartItemRepository.deleteByCartId(cart.getId());
        return new CartResponseDTO(List.of(), 0.0, 0.0, 0.0);
    }

    /**
     * Processes the checkout by calculating the subtotal, applying discount (if any),
     * and computing the final total. After a successful checkout, the cart is emptied.
     */
    @Transactional
    public CheckoutResponseDTO checkout(String userEmail, CheckoutRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
        Cart cart = cartRepository.findByUserIdWithItems(user.getId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Cannot checkout an empty cart");
        }

        double subtotal = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        double discount = applyDiscount(user, subtotal, request.getCouponCode());
        double total = subtotal - discount;

        // Simulate successful checkout by emptying the cart.
        cartItemRepository.deleteByCartId(cart.getId());

        return new CheckoutResponseDTO(subtotal, discount, total, "Checkout successful!");
    }

    /**
     * Helper method to build the CartResponseDTO.
     *
     * @param cart     The current cart.
     * @param discount The discount to be applied.
     * @return A CartResponseDTO with the list of items, subtotal, discount, and total.
     */
    private CartResponseDTO getCartResponse(Cart cart, double discount) {
        List<CartItemResponseDTO> items = cart.getItems().stream().map(item -> new CartItemResponseDTO(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity()
        )).collect(Collectors.toList());

        double subtotal = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        double total = subtotal - discount;

        return new CartResponseDTO(items, subtotal, discount, total);
    }

    /**
     * Calculates the discount based on the subtotal.
     * If a valid coupon is provided, the coupon’s discount is applied if it exceeds the default discount.
     */
    private double applyDiscount(User user, double subtotal, String couponCode) {
        double defaultDiscount = 0.0;
        if (subtotal >= 1000) {
            defaultDiscount = subtotal * 0.30;
        } else if (subtotal >= 500) {
            defaultDiscount = subtotal * 0.20;
        } else if (subtotal >= 100) {
            defaultDiscount = subtotal * 0.10;
        }

        if (couponCode != null) {
            Optional<Coupon> couponOptional = couponRepository.findById(couponCode);
            if (couponOptional.isPresent()) {
                Coupon coupon = couponOptional.get();
                if (coupon.isActive() && coupon.getExpiryDate().isAfter(java.time.LocalDateTime.now())) {
                    // Either the coupon is global or belongs to the current user.
                    if (coupon.getUser() == null || coupon.getUser().equals(user)) {
                        double couponDiscount = subtotal * (coupon.getDiscountPercentage() / 100.0);
                        return Math.max(defaultDiscount, couponDiscount);
                    }
                }
            }
        }
        return defaultDiscount;
    }
}
