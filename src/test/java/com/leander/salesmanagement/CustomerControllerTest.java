
package com.leander.salesmanagement;

import com.leander.salesmanagement.controller.CustomerController;
import com.leander.salesmanagement.entity.Customer;
import com.leander.salesmanagement.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    private CustomerService customerService;
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        customerService = mock(CustomerService.class);
        customerController = new CustomerController(customerService);
    }

    @Test
    void createCustomerShouldReturnCreatedCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("Customer");
        customer.setEmail("test@example.com");

        when(customerService.createCustomer(customer))
                .thenReturn(customer);

        Customer result = customerController.createCustomer(customer);

        assertNotNull(result);
        assertEquals("Test", result.getFirstName());
        assertEquals("Customer", result.getLastName());
        assertEquals("test@example.com", result.getEmail());

        verify(customerService).createCustomer(customer);
    }

    @Test
    void getAllCustomersShouldReturnCustomerList() {
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("Customer");
        customer.setEmail("test@example.com");

        when(customerService.getAllCustomers())
                .thenReturn(List.of(customer));

        List<Customer> result = customerController.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getFirstName());

        verify(customerService).getAllCustomers();
    }

    @Test
    void getCustomerByIdShouldReturnCustomer() {
        Long customerId = 1L;

        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("Customer");
        customer.setEmail("test@example.com");

        when(customerService.getCustomerById(customerId))
                .thenReturn(customer);

        Customer result = customerController.getCustomerById(customerId);

        assertNotNull(result);
        assertEquals("Test", result.getFirstName());

        verify(customerService).getCustomerById(customerId);
    }

    @Test
    void getCustomerByIdShouldPropagateServiceException() {
        Long customerId = 999L;

        when(customerService.getCustomerById(customerId))
                .thenThrow(new RuntimeException("Customer not found"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> customerController.getCustomerById(customerId)
        );

        assertEquals("Customer not found", exception.getMessage());
        verify(customerService).getCustomerById(customerId);
    }
}
