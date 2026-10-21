
package com.leander.salesmanagement.controller;

import com.leander.salesmanagement.entity.Product;
import com.leander.salesmanagement.service.ProductFileUploadService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductUploadController {

	private final ProductFileUploadService productFileUploadService;

	public ProductUploadController(ProductFileUploadService productFileUploadService) {
		this.productFileUploadService = productFileUploadService;
	}

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	@ResponseStatus(HttpStatus.CREATED)
	public List<Product> uploadProducts(@RequestParam("file") MultipartFile file) throws IOException {

		return productFileUploadService.uploadProducts(file);
	}
}
