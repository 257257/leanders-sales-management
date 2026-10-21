package com.leander.salesmanagement;

import com.leander.salesmanagement.entity.Customer;
import com.leander.salesmanagement.repository.CustomerRepository;
import com.leander.salesmanagement.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void createCustomerShouldSaveValidCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("Customer");
        customer.setEmail("test@example.com");

        when(customerRepository.existsByEmail("test@example.com"))
                .thenReturn(false);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.createCustomer(customer);

        assertNotNull(result);
        assertEquals("Test", result.getFirstName());
        assertEquals("Customer", result.getLastName());
        assertEquals("test@example.com", result.getEmail());

        verify(customerRepository).save(customer);
    }

    @Test
    void createCustomerShouldRejectMissingFirstName() {
        Customer customer = new Customer();
        customer.setLastName("Customer");
        customer.setEmail("test@example.com");

        assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(customer)
        );

        verifyNoInteractions(customerRepository);
    }

    @Test
    void createCustomerShouldRejectMissingLastName() {
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setEmail("test@example.com");

        assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(customer)
        );

        verifyNoInteractions(customerRepository);
    }

    @Test
    void createCustomerShouldRejectMissingEmail() {
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("Customer");

        assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(customer)
        );

        verifyNoInteractions(customerRepository);
    }

    @Test
    void createCustomerShouldRejectDuplicateEmail() {
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("Customer");
        customer.setEmail("test@example.com");

        when(customerRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(customer)
        );

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getAllCustomersShouldReturnCustomers() {
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("Customer");
        customer.setEmail("test@example.com");

        when(customerRepository.findAll())
                .thenReturn(List.of(customer));

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
    }

    @Test
    void getCustomerByIdShouldReturnCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Test");
        customer.setLastName("Customer");
        customer.setEmail("test@example.com");

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals("Test", result.getFirstName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getCustomerByIdShouldRejectMissingCustomer() {
        when(customerRepository.findById(99L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.getCustomerById(99L)
        );

        assertEquals(
                "Customer not found with ID: 99",
                exception.getMessage()
        );
    }
}