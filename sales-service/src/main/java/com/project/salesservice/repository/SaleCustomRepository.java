package com.project.salesservice.repository;

import com.project.salesservice.dto.SaleFilterDTO;
import com.project.salesservice.entity.Sale;

import java.util.List;

public interface SaleCustomRepository {

    List<Sale> findSalesByCriteria(SaleFilterDTO saleFilter);
}
