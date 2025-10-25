package com.hugo.product.controller;

import com.hugo.product.dto.ProductCreateDTO;
import com.hugo.product.dto.ProductResponseDTO;
import com.hugo.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        Optional<ProductResponseDTO> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductCreateDTO createDTO) {
        ProductResponseDTO createdProduct = productService.createProduct(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id, 
            @RequestBody ProductCreateDTO updateDTO) {
        Optional<ProductResponseDTO> updatedProduct = productService.updateProduct(id, updateDTO);
        return updatedProduct.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String name) {
        List<ProductResponseDTO> products = productService.searchProducts(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<ProductResponseDTO>> getProductsInStock() {
        List<ProductResponseDTO> products = productService.getProductsInStock();
        return ResponseEntity.ok(products);
    }
}