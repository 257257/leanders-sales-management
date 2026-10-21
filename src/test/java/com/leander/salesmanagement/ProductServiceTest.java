package com.leander.salesmanagement;

import com.leander.salesmanagement.entity.Product;
import com.leander.salesmanagement.repository.ProductRepository;
import com.leander.salesmanagement.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Product for unit testing");
        product.setPrice(new BigDecimal("50.00"));
        product.setQuantity(10);
        return product;
    }

    @Test
    void createProductShouldSaveValidProduct() {
        Product product = createTestProduct();

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(new BigDecimal("50.00"), result.getPrice());
        assertEquals(10, result.getQuantity());

        verify(productRepository).save(product);
    }

    @Test
    void createProductShouldRejectMissingName() {
        Product product = createTestProduct();
        product.setName(" ");

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(product)
        );

        verifyNoInteractions(productRepository);
    }

    @Test
    void createProductShouldRejectNullPrice() {
        Product product = createTestProduct();
        product.setPrice(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(product)
        );

        verifyNoInteractions(productRepository);
    }

    @Test
    void createProductShouldRejectNegativePrice() {
        Product product = createTestProduct();
        product.setPrice(new BigDecimal("-1.00"));

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(product)
        );

        verifyNoInteractions(productRepository);
    }

    @Test
    void createProductShouldRejectNullQuantity() {
        Product product = createTestProduct();
        product.setQuantity(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(product)
        );

        verifyNoInteractions(productRepository);
    }

    @Test
    void createProductShouldRejectNegativeQuantity() {
        Product product = createTestProduct();
        product.setQuantity(-1);

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(product)
        );

        verifyNoInteractions(productRepository);
    }

    @Test
    void createProductShouldAllowZeroPriceAndQuantity() {
        Product product = createTestProduct();
        product.setPrice(BigDecimal.ZERO);
        product.setQuantity(0);

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertEquals(BigDecimal.ZERO, result.getPrice());
        assertEquals(0, result.getQuantity());
        verify(productRepository).save(product);
    }

    @Test
    void getAllProductsShouldReturnProducts() {
        Product product = createTestProduct();

        when(productRepository.findAll())
                .thenReturn(List.of(product));

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
    }

    @Test
    void getProductByIdShouldReturnProduct() {
        Product product = createTestProduct();

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }

    @Test
    void getProductByIdShouldRejectMissingProduct() {
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.getProductById(99L)
        );

        assertEquals(
                "Product not found with ID: 99",
                exception.getMessage()
        );
    }

    @Test
    void searchProductsByNameShouldReturnMatchingProducts() {
        Product product = createTestProduct();

        when(productRepository.findByNameContainingIgnoreCase("test"))
                .thenReturn(List.of(product));

        List<Product> result =
                productService.searchProductsByName("test");

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
    }

    @Test
    void updateProductShouldUpdateExistingProduct() {
        Product existingProduct = createTestProduct();

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated description");
        updatedProduct.setPrice(new BigDecimal("75.00"));
        updatedProduct.setQuantity(20);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct))
                .thenReturn(existingProduct);

        Product result =
                productService.updateProduct(1L, updatedProduct);

        assertEquals("Updated Product", result.getName());
        assertEquals("Updated description", result.getDescription());
        assertEquals(new BigDecimal("75.00"), result.getPrice());
        assertEquals(20, result.getQuantity());

        verify(productRepository).save(existingProduct);
    }

    @Test
    void updateProductShouldRejectMissingName() {
        Product existingProduct = createTestProduct();
        Product updatedProduct = createTestProduct();
        updatedProduct.setName(" ");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existingProduct));

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.updateProduct(1L, updatedProduct)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProductShouldRejectNegativePrice() {
        Product existingProduct = createTestProduct();
        Product updatedProduct = createTestProduct();
        updatedProduct.setPrice(new BigDecimal("-1.00"));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existingProduct));

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.updateProduct(1L, updatedProduct)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProductShouldRejectNegativeQuantity() {
        Product existingProduct = createTestProduct();
        Product updatedProduct = createTestProduct();
        updatedProduct.setQuantity(-1);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existingProduct));

        assertThrows(
                IllegalArgumentException.class,
                () -> productService.updateProduct(1L, updatedProduct)
        );

        verify(productRepository, never()).save(any(Product.class));
    }
}