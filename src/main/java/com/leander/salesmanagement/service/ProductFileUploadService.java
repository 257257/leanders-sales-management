
package com.leander.salesmanagement.service;

import com.leander.salesmanagement.entity.Product;
import com.leander.salesmanagement.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductFileUploadService {

	private final ProductRepository productRepository;

	public ProductFileUploadService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional
	public List<Product> uploadProducts(MultipartFile file) throws IOException {

		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("Please upload a non-empty file.");
		}

		List<Product> products = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

			String line;
			int lineNumber = 0;

			while ((line = reader.readLine()) != null) {
				lineNumber++;

				if (line.isBlank()) {
					continue;
				}

				String[] fields = line.split("\\|", -1);

				if (fields.length != 4) {
					throw new IllegalArgumentException(
							"Invalid format on line " + lineNumber + ". Expected: name|description|price|quantity");
				}

				String name = fields[0].trim();
				String description = fields[1].trim();
				String priceText = fields[2].trim();
				String quantityText = fields[3].trim();

				if (name.isEmpty()) {
					throw new IllegalArgumentException("Product name is required on line " + lineNumber + ".");
				}

				try {
					BigDecimal price = new BigDecimal(priceText);
					int quantity = Integer.parseInt(quantityText);

					if (price.compareTo(BigDecimal.ZERO) < 0) {
						throw new IllegalArgumentException("Price cannot be negative on line " + lineNumber + ".");
					}

					if (quantity < 0) {
						throw new IllegalArgumentException("Quantity cannot be negative on line " + lineNumber + ".");
					}

					Product product = new Product();
					product.setName(name);
					product.setDescription(description);
					product.setPrice(price);
					product.setQuantity(quantity);

					products.add(product);

				} catch (NumberFormatException exception) {
					throw new IllegalArgumentException("Invalid price or quantity on line " + lineNumber + ".");
				}
			}
		}

		if (products.isEmpty()) {
			throw new IllegalArgumentException("The file contains no valid product rows.");
		}

		return productRepository.saveAll(products);
	}
}
