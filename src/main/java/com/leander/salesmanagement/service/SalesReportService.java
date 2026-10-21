package com.leander.salesmanagement.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.leander.salesmanagement.dto.SaleResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class SalesReportService {

	private final SaleService saleService;

	public SalesReportService(SaleService saleService) {
		this.saleService = saleService;
	}

	public byte[] generateSalesReport(LocalDateTime startDate, LocalDateTime endDate) {

		try {
			// Fetch sales using the existing service
			List<SaleResponse> sales = saleService.getSalesByDateRange(startDate, endDate);

			// Convert the sales list into a JasperReports data source
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(sales);

			// Pass the selected date range to the report
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("START_DATE", startDate.toString());
			parameters.put("END_DATE", endDate.toString());

			// Load the JRXML file from src/main/resources
			try (InputStream inputStream = SalesReportService.class
					.getResourceAsStream("/reports/sales_report.jrxml")) {

				if (inputStream == null) {
					throw new IllegalStateException(
							"Sales report template was not found at " + "/reports/sales_report.jrxml");
				}

				// Compile the report template
				JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

				// Fill the report with sales data and parameters
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

				// Export the completed report to PDF bytes
				return JasperExportManager.exportReportToPdf(jasperPrint);
			}

		} catch (JRException | IOException exception) {
			throw new IllegalStateException("Failed to generate the sales PDF report.", exception);
		}
	}
}