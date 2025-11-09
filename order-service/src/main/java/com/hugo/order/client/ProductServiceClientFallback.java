package com.hugo.order.client;

import com.hugo.common.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
public class ProductServiceClientFallback implements ProductServiceClient {
    
    @Override
    public ProductDTO getProductById(Long id) {
        ProductDTO fallbackProduct = new ProductDTO();
        fallbackProduct.setId(id);
        fallbackProduct.setName("Produto Indisponível");
        fallbackProduct.setDescription("Serviço de produtos temporariamente indisponível");
        fallbackProduct.setPrice(BigDecimal.ZERO);
        fallbackProduct.setStockQuantity(0);
        return fallbackProduct;
    }
    
    @Override
    public List<ProductDTO> getAllProducts() {
        return Collections.emptyList();
    }
    
    @Override
    public List<ProductDTO> getProductsInStock() {
        return Collections.emptyList();
    }
    
    @Override
    public List<ProductDTO> searchProducts(String name) {
        return Collections.emptyList();
    }
}