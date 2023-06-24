package com.inventoryService.service.impl;

import com.inventoryService.entity.Product;
import com.inventoryService.repository.CustomerRepository;
import com.inventoryService.repository.ProductRepository;
import com.inventoryService.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Integer productId)
    {
        return productRepository.findById(productId).get();
    }

    @Override
    public void addProduct(String customerId, Product product)
    {
        if(customerRepository.findById(customerId).get().isAdmin())
            productRepository.save(product);
        else
        {
            log.info("User not permitted to add the product");
        }
    }

    @Override
    public void deleteProd(Integer productId, String customerId)
    {
        if(customerRepository.findById(customerId).get().isAdmin())
        productRepository.deleteById(productId);
        else
        {
            log.info("User not permitted to delete the product");
        }
    }
}
