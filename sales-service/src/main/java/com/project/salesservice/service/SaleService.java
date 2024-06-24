package com.project.salesservice.service;

import com.project.salesservice.entity.Sale;

import java.util.List;

public interface SaleService {

    List<Sale> findAllSales();

    Sale findSale(Long id);

    Sale getSaleForUser(Long id,Long userId);

    Sale saveSale(Sale sale);

    void deleteSale(Long id);
}
