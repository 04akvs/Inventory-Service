package com.inventoryService.service;

import com.inventoryService.entity.Customer;

public interface CustomerService {

    Customer getCustomerById(String id);

    void addCustomer(Customer customer);

    boolean isAdmin(String id);

    void deleteCustomer(String customerId);
}