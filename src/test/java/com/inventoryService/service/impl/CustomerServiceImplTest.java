package com.inventoryService.service.impl;

import com.inventoryService.entity.Customer;
import com.inventoryService.repository.CustomerRepository;
import com.inventoryService.service.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerService customerService = new CustomerServiceImpl();
    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void getCustomerByIdTest()
    {
        Customer customer = new Customer("3", "Jenna", "jenna@yahoo.com", "93211673637", true);
        when(customerRepository.findById("3")).thenReturn(Optional.of(customer));
        assertEquals(customer, customerService.getCustomerById("3"));
    }

    @Test
    public void addCustomerTest()
    {
        Customer customer = new Customer("1", "Milly", "milly@yahoo.com", "91211673637", false);
        when(customerRepository.save(customer)).thenReturn(customer);

        customerService.addCustomer(customer);
        verify(customerRepository,times(1)).save(customer);
    }

    @Test
    public void whenUserisAdminTest()
    {
        Customer customer = new Customer();
        customer.setAdmin(true);
        when(customerRepository.findById("3")).thenReturn(Optional.of(customer));

        assertTrue(customerService.isAdmin("3"));
    }

    @Test
    public void whenUserisNotAdminTest()
    {
        Customer customer = new Customer();
        customer.setAdmin(false);
        when(customerRepository.findById("2")).thenReturn(Optional.of(customer));

        assertFalse(customerService.isAdmin("2"));
    }
    @Test
    public void deleteCustomer()
    {
        customerService.deleteCustomer("3");
        verify(customerRepository,times(1)).deleteById("3");
    }
}
