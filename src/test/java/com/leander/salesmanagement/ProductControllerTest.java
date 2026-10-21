
package com.leander.salesmanagement;

import com.leander.salesmanagement.controller.ProductController;
import com.leander.salesmanagement.entity.Product;
import com.leander.salesmanagement.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    private ProductService productService;
    private ProductController productController;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        productController = new ProductController(productService);
    }

    @Test
    void createProductShouldReturnCreatedProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test description");
        product.setPrice(new BigDecimal("99.90"));
        product.setQuantity(10);

        when(productService.createProduct(product)).thenReturn(product);

        Product result = productController.createProduct(product);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals("Test description", result.getDescription());
        assertEquals(new BigDecimal("99.90"), result.getPrice());
        assertEquals(10, result.getQuantity());

        verify(productService).createProduct(product);
    }

    @Test
    void getAllProductsShouldReturnProductList() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test description");
        product.setPrice(new BigDecimal("99.90"));
        product.setQuantity(10);

        when(productService.getAllProducts()).thenReturn(List.of(product));

        List<Product> result = productController.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());

        verify(productService).getAllProducts();
    }

    @Test
    void getProductByIdShouldReturnProduct() {
        Long productId = 1L;

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test description");
        product.setPrice(new BigDecimal("99.90"));
        product.setQuantity(10);

        when(productService.getProductById(productId)).thenReturn(product);

        Product result = productController.getProductById(productId);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(new BigDecimal("99.90"), result.getPrice());

        verify(productService).getProductById(productId);
    }

    @Test
    void searchProductsByNameShouldReturnMatchingProducts() {
        String name = "Test";

        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test description");
        product.setPrice(new BigDecimal("99.90"));
        product.setQuantity(10);

        when(productService.searchProductsByName(name)).thenReturn(List.of(product));

        List<Product> result = productController.searchProductsByName(name);

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        assertEquals(new BigDecimal("99.90"), result.get(0).getPrice());

        verify(productService).searchProductsByName(name);
    }

    @Test
    void updateProductShouldReturnUpdatedProduct() {
        Long productId = 1L;

        Product product = new Product();
        product.setName("Updated Product");
        product.setDescription("Updated description");
        product.setPrice(new BigDecimal("149.90"));
        product.setQuantity(20);

        when(productService.updateProduct(productId, product)).thenReturn(product);

        Product result = productController.updateProduct(productId, product);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated description", result.getDescription());
        assertEquals(new BigDecimal("149.90"), result.getPrice());
        assertEquals(20, result.getQuantity());

        verify(productService).updateProduct(productId, product);
    }

    @Test
    void getProductByIdShouldPropagateServiceException() {
        Long productId = 999L;

        when(productService.getProductById(productId))
                .thenThrow(new RuntimeException("Product not found"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productController.getProductById(productId)
        );

        assertEquals("Product not found", exception.getMessage());
        verify(productService).getProductById(productId);
    }
}
