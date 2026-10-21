
package com.leander.salesmanagement;

import com.leander.salesmanagement.controller.SaleController;
import com.leander.salesmanagement.dto.SaleResponse;
import com.leander.salesmanagement.entity.Sale;
import com.leander.salesmanagement.service.SaleService;
import com.leander.salesmanagement.service.SalesReportService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleControllerTest {

    @Mock
    private SaleService saleService;

    @Mock
    private SalesReportService salesReportService;

    @InjectMocks
    private SaleController saleController;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.of(2026, 10, 21, 0, 0);
        endDate = LocalDateTime.of(2026, 10, 21, 23, 59, 59);
    }

    @Test
    void createSaleReturnsCreatedSale() {
        SaleController.CreateSaleRequest request =
                new SaleController.CreateSaleRequest();

        request.setCustomerId(1L);
        request.setProductId(2L);
        request.setQuantity(3);

        Sale expectedSale = new Sale();

        when(saleService.createSale(1L, 2L, 3))
                .thenReturn(expectedSale);

        Sale result = saleController.createSale(request);

        assertSame(expectedSale, result);
        verify(saleService).createSale(1L, 2L, 3);
    }

    @Test
    void getAllSalesReturnsSalesList() {
        List<SaleResponse> expectedSales = List.of(
                mock(SaleResponse.class),
                mock(SaleResponse.class)
        );

        when(saleService.getAllSales()).thenReturn(expectedSales);

        List<SaleResponse> result = saleController.getAllSales();

        assertEquals(expectedSales, result);
        verify(saleService).getAllSales();
    }

    @Test
    void getSaleByIdReturnsSale() {
        SaleResponse expectedSale = mock(SaleResponse.class);

        when(saleService.getSaleById(1L)).thenReturn(expectedSale);

        SaleResponse result = saleController.getSaleById(1L);

        assertSame(expectedSale, result);
        verify(saleService).getSaleById(1L);
    }

    @Test
    void getSalesByCustomerReturnsSalesList() {
        List<SaleResponse> expectedSales =
                List.of(mock(SaleResponse.class));

        when(saleService.getSalesByCustomer(1L))
                .thenReturn(expectedSales);

        List<SaleResponse> result =
                saleController.getSalesByCustomer(1L);

        assertEquals(expectedSales, result);
        verify(saleService).getSalesByCustomer(1L);
    }

    @Test
    void getSalesByProductReturnsSalesList() {
        List<SaleResponse> expectedSales =
                List.of(mock(SaleResponse.class));

        when(saleService.getSalesByProduct(2L))
                .thenReturn(expectedSales);

        List<SaleResponse> result =
                saleController.getSalesByProduct(2L);

        assertEquals(expectedSales, result);
        verify(saleService).getSalesByProduct(2L);
    }

    @Test
    void getSalesByDateRangeReturnsSalesList() {
        List<SaleResponse> expectedSales =
                List.of(mock(SaleResponse.class));

        when(saleService.getSalesByDateRange(startDate, endDate))
                .thenReturn(expectedSales);

        List<SaleResponse> result =
                saleController.getSalesByDateRange(startDate, endDate);

        assertEquals(expectedSales, result);
        verify(saleService).getSalesByDateRange(startDate, endDate);
    }

    @Test
    void getSalesByDateRangeThrowsExceptionWhenStartDateIsAfterEndDate() {
        LocalDateTime invalidStartDate =
                LocalDateTime.of(2026, 10, 22, 0, 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> saleController.getSalesByDateRange(
                        invalidStartDate, endDate)
        );

        assertEquals(
                "Start date must not be after end date.",
                exception.getMessage()
        );

        verifyNoInteractions(saleService);
    }

    @Test
    void downloadSalesReportReturnsPdfResponse() {
        byte[] expectedPdf = {1, 2, 3, 4};

        when(salesReportService.generateSalesReport(startDate, endDate))
                .thenReturn(expectedPdf);

        ResponseEntity<byte[]> response =
                saleController.downloadSalesReport(startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(expectedPdf, response.getBody());
        assertEquals(
                MediaType.APPLICATION_PDF,
                response.getHeaders().getContentType()
        );
        assertEquals(
                "attachment; filename=\"sales-report.pdf\"",
                response.getHeaders().getFirst(
                        HttpHeaders.CONTENT_DISPOSITION)
        );

        verify(salesReportService)
                .generateSalesReport(startDate, endDate);
    }

    @Test
    void downloadSalesReportThrowsExceptionWhenStartDateIsAfterEndDate() {
        LocalDateTime invalidStartDate =
                LocalDateTime.of(2026, 10, 22, 0, 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> saleController.downloadSalesReport(
                        invalidStartDate, endDate)
        );

        assertEquals(
                "Start date must not be after end date.",
                exception.getMessage()
        );

        verifyNoInteractions(salesReportService);
    }
}
