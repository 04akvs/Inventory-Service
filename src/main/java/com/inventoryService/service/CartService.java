package com.inventoryService.service;

import com.inventoryService.entity.Product;

import java.util.List;

public interface CartService {


    Product addProductInCart(String customerId, Integer productId);

    List<Product> getAllProductsInCart(String customerId);

    List<Product> deleteProdFromCart(String customerId, Integer productId);

    void deleteCart(String customerId);

}
