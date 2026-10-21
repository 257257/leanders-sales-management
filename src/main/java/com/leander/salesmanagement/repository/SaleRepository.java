
package com.leander.salesmanagement.repository;

import com.leander.salesmanagement.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	List<Sale> findByCustomerId(Long customerId);

	List<Sale> findByProductId(Long productId);
}
