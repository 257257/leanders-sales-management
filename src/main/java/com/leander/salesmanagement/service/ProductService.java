
package com.leander.salesmanagement.service;

import com.leander.salesmanagement.entity.Product;
import com.leander.salesmanagement.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product createProduct(Product product) {

		if (product.getName() == null || product.getName().isBlank()) {
			throw new IllegalArgumentException("Product name is required.");
		}

		if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Product price must be zero or greater.");
		}

		if (product.getQuantity() == null || product.getQuantity() < 0) {
			throw new IllegalArgumentException("Product quantity cannot be negative.");
		}

		return productRepository.save(product);
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
	}

	public List<Product> searchProductsByName(String name) {
		return productRepository.findByNameContainingIgnoreCase(name);
	}

	public Product updateProduct(Long id, Product updatedProduct) {

		Product existingProduct = getProductById(id);

		if (updatedProduct.getName() == null || updatedProduct.getName().isBlank()) {
			throw new IllegalArgumentException("Product name is required.");
		}

		if (updatedProduct.getPrice() == null || updatedProduct.getPrice().compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Product price must be zero or greater.");
		}

		if (updatedProduct.getQuantity() == null || updatedProduct.getQuantity() < 0) {
			throw new IllegalArgumentException("Product quantity cannot be negative.");
		}

		existingProduct.setName(updatedProduct.getName());
		existingProduct.setDescription(updatedProduct.getDescription());
		existingProduct.setPrice(updatedProduct.getPrice());
		existingProduct.setQuantity(updatedProduct.getQuantity());

		return productRepository.save(existingProduct);
	}
}
