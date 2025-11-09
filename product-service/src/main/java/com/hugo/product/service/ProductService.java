package com.hugo.product.service;

import com.hugo.product.dto.ProductCreateDTO;
import com.hugo.product.dto.ProductResponseDTO;
import com.hugo.product.model.Product;
import com.hugo.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    private final ProductEventPublisher eventPublisher;
    
    @Autowired
    public ProductService(ProductRepository productRepository, ProductEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }
    
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<ProductResponseDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToResponseDTO);
    }
    
    public ProductResponseDTO createProduct(ProductCreateDTO createDTO) {
        Product product = convertToEntity(createDTO);
        Product savedProduct = productRepository.save(product);
        
        eventPublisher.publishProductCreated(savedProduct);
        
        return convertToResponseDTO(savedProduct);
    }
    
    public Optional<ProductResponseDTO> updateProduct(Long id, ProductCreateDTO updateDTO) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updateDTO.getName());
                    existingProduct.setDescription(updateDTO.getDescription());
                    existingProduct.setPrice(updateDTO.getPrice());
                    existingProduct.setStockQuantity(updateDTO.getStockQuantity());
                    Product savedProduct = productRepository.save(existingProduct);
                    
                    eventPublisher.publishProductUpdated(savedProduct);
                    
                    if (savedProduct.getStockQuantity() < 10) {
                        eventPublisher.publishStockLow(savedProduct);
                    }
                    
                    return convertToResponseDTO(savedProduct);
                });
    }
    
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            Product product = productRepository.findById(id).orElse(null);
            if (product != null) {
                String productName = product.getName();
                productRepository.deleteById(id);
                
                eventPublisher.publishProductDeleted(id, productName);
                
                return true;
            }
        }
        return false;
    }
    
    public List<ProductResponseDTO> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductResponseDTO> getProductsInStock() {
        return productRepository.findByStockQuantityGreaterThan(0)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    private ProductResponseDTO convertToResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }
    
    private Product convertToEntity(ProductCreateDTO createDTO) {
        Product product = new Product();
        product.setName(createDTO.getName());
        product.setDescription(createDTO.getDescription());
        product.setPrice(createDTO.getPrice());
        product.setStockQuantity(createDTO.getStockQuantity());
        return product;
    }
}