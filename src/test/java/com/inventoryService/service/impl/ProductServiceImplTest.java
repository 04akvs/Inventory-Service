package com.inventoryService.service.impl;

import com.inventoryService.entity.Customer;
import com.inventoryService.entity.Product;
import com.inventoryService.repository.CustomerRepository;
import com.inventoryService.repository.ProductRepository;
import com.inventoryService.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductService productService = new ProductServiceImpl();
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void getAllProductsTest()
    {
        when(productRepository.findAll()).thenReturn(Stream.of(new Product(1, "Sunsilk", 230, "Shampoo", 1),
                new Product(2, "Yardley London", 550, "Perfume", 2)).collect(Collectors.toList()));
        assertEquals(2, productService.getAllProducts().size());
    }

    @Test
    public void getProductByIdTest()
    {
        Product product = new Product(1, "Sunsilk", 230, "Shampoo", 1);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        assertEquals(product, productService.getProductById(1));
    }

    @Test
    public void addProductWhenUserIsAdmin()
    {
        Customer customer = new Customer();
        customer.setAdmin(true);
        when(customerRepository.findById(any(String.class))).thenReturn(Optional.of(customer));

        productService.addProduct("1",new Product());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void addProductWhenUserIsNotAdmin()
    {
        Customer customer = new Customer();
        customer.setAdmin(false);
        when(customerRepository.findById(any(String.class))).thenReturn(Optional.of(customer));

        productService.addProduct("2", new Product());
       verify(productRepository, times(0)).save(any(Product.class));
       //verify(log,times(1)).info("User not permitted to add the product");
    }

    @Test
    public void deleteProductWhenUserIsAdmin()
    {
        Customer customer = new Customer();
        customer.setAdmin(true);
        when(customerRepository.findById(any(String.class))).thenReturn(Optional.of(customer));

        productService.deleteProd(1,"1");
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    public void deleteProductWhenUserIsNotAdmin()
    {
        Customer customer = new Customer();
        customer.setAdmin(false);
        when(customerRepository.findById(any(String.class))).thenReturn(Optional.of(customer));

        productService.deleteProd(1,"2");
        verify(productRepository, times(0)).deleteById(1);
    }
}
