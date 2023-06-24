package com.inventoryService.service.impl;

import com.inventoryService.entity.Customer;
import com.inventoryService.repository.CustomerRepository;
import com.inventoryService.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer getCustomerById(String id)
    {
        return customerRepository.findById(id).get();
    }

    public void addCustomer(Customer customer)
    {
        customerRepository.save(customer);
    }

    public boolean isAdmin(String id)
    {
        Customer customer = customerRepository.findById(id).get();
        return customer.isAdmin();
    }

    public void deleteCustomer(String customerId)
    {
        customerRepository.deleteById(customerId);
    }
}
