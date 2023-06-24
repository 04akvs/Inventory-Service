package com.inventoryService.controller;

import com.inventoryService.entity.Customer;
import com.inventoryService.entity.Orders;
import com.inventoryService.entity.Product;
import com.inventoryService.service.CartService;
import com.inventoryService.service.CustomerService;
import com.inventoryService.service.OrderService;
import com.inventoryService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId) {
        try {
            Customer Customer = customerService.getCustomerById(customerId);
            return new ResponseEntity<Customer>(Customer, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/customers/add_customer")
    public void addCustomer(@RequestBody Customer customer)
    {
        customerService.addCustomer(customer);
    }

    @PutMapping("/customers/update_customer/{customerId}")
    public ResponseEntity<Customer> update(@RequestBody Customer customer, @PathVariable String customerId) {
        try {
            Customer existCustomer = customerService.getCustomerById(customerId);
            customerService.addCustomer(customer);
            return new ResponseEntity<Customer>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/customers/delete_customer/{customerId}")
    public ResponseEntity<?> delete(@PathVariable String customerId) {
        try {
            Customer existCustomer = customerService.getCustomerById(customerId);
            customerService.deleteCustomer(customerId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/customers/get_all_products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/customers/add_product_in_cart/{customerId}/{productId}")
    public Product addProductInCart(@PathVariable String customerId, @PathVariable Integer productId) {
        return cartService.addProductInCart(customerId, productId);
    }

    @GetMapping("/customers/get_all_products_in_cart/{customerId}")
    public List<Product> getAllProductsInCart(@PathVariable String customerId) {
        return cartService.getAllProductsInCart(customerId);
    }

    @DeleteMapping("/customers/delete_product_in_cart/{customerId}/{productId}")
    public List<Product> deleteProductInCart(@PathVariable String customerId, @PathVariable Integer productId)
    {
        return cartService.deleteProdFromCart(customerId, productId);
    }

    @GetMapping("/customers/get_amount_to_be_paid/{customerId}")
    public double getAmountToBePaid(@PathVariable String customerId)
    {
        return orderService.getAmountToBePaid(customerId);
    }

    @GetMapping("/customers/get_bill_info/{customerId}")
    public Orders obtainBillInfo(@PathVariable String customerId)
    {
        return orderService.obtainBillInfo(customerId);
    }

    @PostMapping("/customers/create_order/{customerId}")
    public ResponseEntity<String> createOrder(@PathVariable String customerId)
    {
        try {
            orderService.createOrder(customerId);
            return new ResponseEntity<>("Order created successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Failed to create the order", HttpStatus.NOT_FOUND);
        }
    }
}
