package com.project.salesservice.repository;

import com.project.salesservice.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale,Long> {

    Optional<Sale> findByIdAndUserId(Long id, Long userId);
}
