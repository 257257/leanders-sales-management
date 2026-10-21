package com.leander.salesmanagement.service;

import com.leander.salesmanagement.dto.SaleResponse;
import com.leander.salesmanagement.entity.Customer;
import com.leander.salesmanagement.entity.Product;
import com.leander.salesmanagement.entity.Sale;
import com.leander.salesmanagement.repository.CustomerRepository;
import com.leander.salesmanagement.repository.ProductRepository;
import com.leander.salesmanagement.repository.SaleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {

	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;
	private final SaleRepository saleRepository;

	public SaleService(CustomerRepository customerRepository, ProductRepository productRepository,
			SaleRepository saleRepository) {

		this.customerRepository = customerRepository;
		this.productRepository = productRepository;
		this.saleRepository = saleRepository;
	}

	@Transactional
	public Sale createSale(Long customerId, Long productId, Integer quantity) {

		if (quantity == null || quantity <= 0) {
			throw new IllegalArgumentException("Sale quantity must be greater than zero.");
		}

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

		if (product.getQuantity() < quantity) {
			throw new IllegalArgumentException("Insufficient stock. Available quantity: " + product.getQuantity());
		}

		BigDecimal unitPrice = product.getPrice();

		BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));

		Sale sale = new Sale();
		sale.setCustomer(customer);
		sale.setProduct(product);
		sale.setQuantity(quantity);
		sale.setUnitPrice(unitPrice);
		sale.setTotalAmount(totalAmount);

		product.setQuantity(product.getQuantity() - quantity);

		productRepository.save(product);

		return saleRepository.save(sale);
	}

	@Transactional(readOnly = true)
	public List<SaleResponse> getAllSales() {
		return saleRepository.findAll().stream().map(this::convertToSaleResponse).toList();
	}

	@Transactional(readOnly = true)
	public SaleResponse convertToSaleResponseForController(Sale sale) {
		return convertToSaleResponse(sale);
	}

	@Transactional(readOnly = true)
	public List<SaleResponse> getSalesByCustomer(Long customerId) {
		return saleRepository.findByCustomerId(customerId).stream().map(this::convertToSaleResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<SaleResponse> getSalesByProduct(Long productId) {
		return saleRepository.findByProductId(productId).stream().map(this::convertToSaleResponse).toList();
	}

	@Transactional(readOnly = true)
	public List<SaleResponse> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {

		return saleRepository.findBySaleDateBetween(startDate, endDate).stream().map(this::convertToSaleResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public SaleResponse getSaleById(Long id) {

		Sale sale = saleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + id));

		return convertToSaleResponse(sale);
	}

	private SaleResponse convertToSaleResponse(Sale sale) {

		SaleResponse response = new SaleResponse();

		response.setId(sale.getId());

		response.setCustomerId(sale.getCustomer().getId());
		response.setCustomerName(sale.getCustomer().getFirstName() + " " + sale.getCustomer().getLastName());

		response.setProductId(sale.getProduct().getId());
		response.setProductName(sale.getProduct().getName());

		response.setQuantity(sale.getQuantity());
		response.setUnitPrice(sale.getUnitPrice());
		response.setTotalAmount(sale.getTotalAmount());
		response.setSaleDate(sale.getSaleDate());

		return response;
	}
}