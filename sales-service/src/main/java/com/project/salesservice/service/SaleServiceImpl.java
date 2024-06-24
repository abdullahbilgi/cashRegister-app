package com.project.salesservice.service;

import com.project.salesservice.entity.Sale;
import com.project.salesservice.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService{

    private final SaleRepository saleRepository;

    @Override
    public List<Sale> findAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public Sale findSale(Long id) {

        Optional<Sale> saleOpt = saleRepository.findById(id);
        if (saleOpt.isEmpty()){
            throw new RuntimeException("There's no sale with id: " + id);
        }

        return saleOpt.get();

    }

    @Override
    public Sale getSaleForUser(Long id, Long userId) {

        Optional<Sale> saleOpt = saleRepository.findByIdAndUserId(id, userId);
        if (saleOpt.isEmpty()){
            throw new RuntimeException(String.format("There's no sale with id: %s and userId: %s",id,userId));
        }

        return saleOpt.get();
    }

    @Override
    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }

    @Override
    public void deleteSale(Long id) {

        saleRepository.deleteById(id);

    }
}
