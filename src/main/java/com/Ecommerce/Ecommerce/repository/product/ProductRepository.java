package com.Ecommerce.Ecommerce.repository.product;

import com.Ecommerce.Ecommerce.model.cart.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    List<Product> findByStockQuantityGreaterThan(int stock);

    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity + :quantity WHERE p.id = :id")
    void restockProduct(@Param("id") Long id, @Param("quantity") int quantity);

    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();
}

//5

