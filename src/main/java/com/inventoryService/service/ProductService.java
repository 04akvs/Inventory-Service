package com.inventoryService.service;

import com.inventoryService.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Integer productId);

    void addProduct(String customerId, Product product);

    void deleteProd(Integer productId, String customerId);
}
