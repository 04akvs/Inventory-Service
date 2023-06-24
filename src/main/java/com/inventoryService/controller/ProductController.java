package com.inventoryService.controller;

import com.inventoryService.entity.Product;
import com.inventoryService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<Product> getAllProducts()
    {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId) {
        try {
            Product product = productService.getProductById(productId);
            return new ResponseEntity<Product>(product, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/products/add_product/{customerId}")
    public void addProduct(@PathVariable String customerId, @RequestBody Product product)
    {
        productService.addProduct(customerId, product);
    }

    @DeleteMapping("/products/delete_product/{productId}/{customerId}")
    public void deleteProduct(@PathVariable Integer productId, @PathVariable String customerId)
    {
        productService.deleteProd(productId, customerId);
    }
    @PutMapping("/products/update_product/{productId}/{customerId}")
    public ResponseEntity<?> update(@RequestBody Product product, @PathVariable Integer productId, @PathVariable String customerId) {
        try {
            Product existProduct = productService.getProductById(productId);
            productService.addProduct(customerId, product);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
