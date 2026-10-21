
package com.leander.salesmanagement.service;

import com.leander.salesmanagement.entity.Customer;
import com.leander.salesmanagement.repository.CustomerRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Customer createCustomer(Customer customer) {

		if (customer.getFirstName() == null || customer.getFirstName().isBlank()) {
			throw new IllegalArgumentException("First name is required.");
		}

		if (customer.getLastName() == null || customer.getLastName().isBlank()) {
			throw new IllegalArgumentException("Last name is required.");
		}

		if (customer.getEmail() == null || customer.getEmail().isBlank()) {
			throw new IllegalArgumentException("Email is required.");
		}

		if (customerRepository.existsByEmail(customer.getEmail())) {
			throw new IllegalArgumentException("A customer with this email already exists.");
		}

		return customerRepository.save(customer);
	}

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public Customer getCustomerById(Long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
	}
}
