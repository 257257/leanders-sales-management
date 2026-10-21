
package com.leander.salesmanagement;

import com.leander.salesmanagement.repository.CustomerRepository;
import com.leander.salesmanagement.repository.ProductRepository;
import com.leander.salesmanagement.repository.SaleRepository;
import com.leander.salesmanagement.service.SaleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class SaleServiceTest {

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private SaleRepository saleRepository;

    private SaleService saleService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        productRepository = mock(ProductRepository.class);
        saleRepository = mock(SaleRepository.class);

        saleService = new SaleService(
                customerRepository,
                productRepository,
                saleRepository
        );
    }

    @Test
    void createSaleShouldRejectNullQuantity() {

        assertThrows(
                IllegalArgumentException.class,
                () -> saleService.createSale(1L, 1L, null)
        );

        verifyNoInteractions(
                customerRepository,
                productRepository,
                saleRepository
        );
    }

    @Test
    void createSaleShouldRejectZeroQuantity() {

        assertThrows(
                IllegalArgumentException.class,
                () -> saleService.createSale(1L, 1L, 0)
        );

        verifyNoInteractions(
                customerRepository,
                productRepository,
                saleRepository
        );
    }

    @Test
    void createSaleShouldRejectNegativeQuantity() {

        assertThrows(
                IllegalArgumentException.class,
                () -> saleService.createSale(1L, 1L, -1)
        );

        verifyNoInteractions(
                customerRepository,
                productRepository,
                saleRepository
        );
    }
}
