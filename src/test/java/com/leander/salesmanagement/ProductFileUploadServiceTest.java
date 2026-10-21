package com.leander.salesmanagement;

import com.leander.salesmanagement.entity.Product;
import com.leander.salesmanagement.repository.ProductRepository;
import com.leander.salesmanagement.service.ProductFileUploadService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ProductFileUploadServiceTest {

    private ProductRepository productRepository;
    private ProductFileUploadService productFileUploadService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productFileUploadService =
                new ProductFileUploadService(productRepository);
    }

    private MockMultipartFile createFile(String content) {
        return new MockMultipartFile(
                "file",
                "products.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    void uploadProductsShouldSaveValidProducts() throws IOException {
        MockMultipartFile file = createFile(
                "Wireless Mouse|Ergonomic mouse|50.00|10\n"
                + "Keyboard|Mechanical keyboard|120.00|5"
        );

        when(productRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<Product> result =
                productFileUploadService.uploadProducts(file);

        assertEquals(2, result.size());

        assertEquals("Wireless Mouse", result.get(0).getName());
        assertEquals("Ergonomic mouse", result.get(0).getDescription());
        assertEquals(0, result.get(0).getPrice()
                .compareTo(new java.math.BigDecimal("50.00")));
        assertEquals(10, result.get(0).getQuantity());

        assertEquals("Keyboard", result.get(1).getName());

        verify(productRepository).saveAll(anyList());
    }

    @Test
    void uploadProductsShouldRejectNullFile() {
        assertThrows(
                IllegalArgumentException.class,
                () -> productFileUploadService.uploadProducts(null)
        );

        verifyNoInteractions(productRepository);
    }

    @Test
    void uploadProductsShouldRejectEmptyFile() {
        MockMultipartFile file = createFile("");

        assertThrows(
                IllegalArgumentException.class,
                () -> productFileUploadService.uploadProducts(file)
        );

        verifyNoInteractions(productRepository);
    }

    @Test
    void uploadProductsShouldRejectInvalidFieldCount() {
        MockMultipartFile file = createFile(
                "Wireless Mouse|Ergonomic mouse|50.00"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productFileUploadService.uploadProducts(file)
        );

        assertTrue(exception.getMessage().contains("Invalid format on line 1"));
        verifyNoInteractions(productRepository);
    }

    @Test
    void uploadProductsShouldRejectMissingProductName() {
        MockMultipartFile file = createFile(
                "|Ergonomic mouse|50.00|10"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productFileUploadService.uploadProducts(file)
        );

        assertTrue(exception.getMessage().contains("Product name is required"));
        verifyNoInteractions(productRepository);
    }

    @Test
    void uploadProductsShouldRejectInvalidPrice() {
        MockMultipartFile file = createFile(
                "Mouse|Ergonomic mouse|invalid|10"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productFileUploadService.uploadProducts(file)
        );

        assertTrue(exception.getMessage().contains("Invalid price or quantity"));
        verifyNoInteractions(productRepository);
    }

    @Test
    void uploadProductsShouldRejectInvalidQuantity() {
        MockMultipartFile file = createFile(
                "Mouse|Ergonomic mouse|50.00|abc"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productFileUploadService.uploadProducts(file)
        );

        assertTrue(exception.getMessage().contains("Invalid price or quantity"));
        verifyNoInteractions(productRepository);
    }

    @Test
    void uploadProductsShouldRejectNegativePrice() {
        MockMultipartFile file = createFile(
                "Mouse|Ergonomic mouse|-5.00|10"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productFileUploadService.uploadProducts(file)
        );

        assertTrue(exception.getMessage().contains("Price cannot be negative"));
        verifyNoInteractions(productRepository);
    }

    @Test
    void uploadProductsShouldRejectNegativeQuantity() {
        MockMultipartFile file = createFile(
                "Mouse|Ergonomic mouse|50.00|-1"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productFileUploadService.uploadProducts(file)
        );

        assertTrue(exception.getMessage().contains("Quantity cannot be negative"));
        verifyNoInteractions(productRepository);
    }

    @Test
    void uploadProductsShouldSkipBlankLines() throws IOException {
        MockMultipartFile file = createFile(
                "\n"
                + "Mouse|Ergonomic mouse|50.00|10\n"
                + "\n"
        );

        when(productRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<Product> result =
                productFileUploadService.uploadProducts(file);

        assertEquals(1, result.size());
        assertEquals("Mouse", result.get(0).getName());
        verify(productRepository).saveAll(anyList());
    }

    @Test
    void uploadProductsShouldRejectFileWithOnlyBlankLines() {
        MockMultipartFile file = createFile("\n   \n");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productFileUploadService.uploadProducts(file)
        );

        assertTrue(exception.getMessage().contains("no valid product rows"));
        verifyNoInteractions(productRepository);
    }
}