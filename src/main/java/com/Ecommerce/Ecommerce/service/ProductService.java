package com.Ecommerce.Ecommerce.service;

import com.Ecommerce.Ecommerce.dataTransfer.product.ProductDTO;
import com.Ecommerce.Ecommerce.model.cart.Product;
import com.Ecommerce.Ecommerce.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO createProduct(ProductDTO productDTO){
        Product product= new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());

        product = productRepository.save(product);
        return mapToDTO(product);
    }

    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());

        product = productRepository.save(product);
        return mapToDTO(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    @Transactional
    public void restockProduct(Long id, int quantity) {
        productRepository.restockProduct(id, quantity);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}
