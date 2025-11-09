package com.hugo.order.client;

import com.hugo.common.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service", fallback = ProductServiceClientFallback.class)
public interface ProductServiceClient {
    
    @GetMapping("/api/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
    
    @GetMapping("/api/products")
    List<ProductDTO> getAllProducts();
    
    @GetMapping("/api/products/in-stock")
    List<ProductDTO> getProductsInStock();
    
    @GetMapping("/api/products/search")
    List<ProductDTO> searchProducts(@RequestParam("name") String name);
}