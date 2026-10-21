
package com.leander.salesmanagement.controller;

import com.leander.salesmanagement.dto.SaleResponse;
import com.leander.salesmanagement.entity.Sale;
import com.leander.salesmanagement.service.SaleService;
import com.leander.salesmanagement.service.SalesReportService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

	private final SaleService saleService;
	private final SalesReportService salesReportService;

	public SaleController(SaleService saleService, SalesReportService salesReportService) {

		this.saleService = saleService;
		this.salesReportService = salesReportService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Sale createSale(@RequestBody CreateSaleRequest request) {

		return saleService.createSale(request.getCustomerId(), request.getProductId(), request.getQuantity());
	}

	@GetMapping
	public List<SaleResponse> getAllSales() {
		return saleService.getAllSales();
	}

	@GetMapping("/{id}")
	public SaleResponse getSaleById(@PathVariable Long id) {
		return saleService.getSaleById(id);
	}

	@GetMapping("/customer/{customerId}")
	public List<SaleResponse> getSalesByCustomer(@PathVariable Long customerId) {

		return saleService.getSalesByCustomer(customerId);
	}

	@GetMapping("/product/{productId}")
	public List<SaleResponse> getSalesByProduct(@PathVariable Long productId) {

		return saleService.getSalesByProduct(productId);
	}

	// JSON sales report endpoint
	@GetMapping("/report")
	public List<SaleResponse> getSalesByDateRange(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("Start date must not be after end date.");
		}

		return saleService.getSalesByDateRange(startDate, endDate);
	}

	// PDF sales report download endpoint
	@GetMapping("/report/pdf")
	public ResponseEntity<byte[]> downloadSalesReport(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("Start date must not be after end date.");
		}

		byte[] pdf = salesReportService.generateSalesReport(startDate, endDate);

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sales-report.pdf\"").body(pdf);
	}

	public static class CreateSaleRequest {

		private Long customerId;
		private Long productId;
		private Integer quantity;

		public CreateSaleRequest() {
		}

		public Long getCustomerId() {
			return customerId;
		}

		public void setCustomerId(Long customerId) {
			this.customerId = customerId;
		}

		public Long getProductId() {
			return productId;
		}

		public void setProductId(Long productId) {
			this.productId = productId;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
	}
}
