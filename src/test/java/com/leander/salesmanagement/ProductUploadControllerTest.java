
package com.leander.salesmanagement;

import com.leander.salesmanagement.controller.ProductUploadController;
import com.leander.salesmanagement.entity.Product;
import com.leander.salesmanagement.service.ProductFileUploadService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductUploadControllerTest {

    private ProductFileUploadService productFileUploadService;
    private ProductUploadController productUploadController;

    @BeforeEach
    void setUp() {
        productFileUploadService = mock(ProductFileUploadService.class);
        productUploadController =
                new ProductUploadController(productFileUploadService);
    }

    @Test
    void uploadProductsShouldReturnUploadedProducts() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "products.txt",
                "text/plain",
                "Test Product|Test description|99.90|10"
                        .getBytes()
        );

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test description");
        product.setPrice(new BigDecimal("99.90"));
        product.setQuantity(10);

        when(productFileUploadService.uploadProducts(file))
                .thenReturn(List.of(product));

        List<Product> result = productUploadController.uploadProducts(file);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        assertEquals(new BigDecimal("99.90"), result.get(0).getPrice());

        verify(productFileUploadService).uploadProducts(file);
    }

    @Test
    void uploadProductsShouldReturnEmptyListWhenNoProductsUploaded()
            throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "products.txt",
                "text/plain",
                new byte[0]
        );

        when(productFileUploadService.uploadProducts(file))
                .thenReturn(List.of());

        List<Product> result = productUploadController.uploadProducts(file);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productFileUploadService).uploadProducts(file);
    }

    @Test
    void uploadProductsShouldPropagateIOException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);

        when(productFileUploadService.uploadProducts(file))
                .thenThrow(new IOException("File upload failed"));

        IOException exception = assertThrows(
                IOException.class,
                () -> productUploadController.uploadProducts(file)
        );

        assertEquals("File upload failed", exception.getMessage());

        verify(productFileUploadService).uploadProducts(file);
    }
}
