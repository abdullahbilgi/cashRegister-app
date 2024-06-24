package com.project.salesservice.facade;

import com.project.salesservice.dto.SaleDTO;
import com.project.salesservice.dto.SaleFilterDTO;
import com.project.salesservice.entity.Sale;
import com.project.salesservice.mapper.SaleMapper;
import com.project.salesservice.repository.SaleCustomRepository;
import com.project.salesservice.repository.SaleCustomRepositoryImpl;
import com.project.salesservice.service.SaleService;
import com.project.salesservice.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaleFacade {

    private final SaleService saleService;
    private final SaleMapper saleMapper;
    private final SaleCustomRepositoryImpl saleCustomRepository;

    public List<SaleDTO> findAllSales(SaleFilterDTO saleFilter){
        return saleCustomRepository.findSalesByCriteria(saleFilter)
                .stream()
                .map(saleMapper::map)
                .toList();
    }

    public List<SaleDTO> listSales(SaleFilterDTO saleFilter){

        saleFilter.setUserId(GeneralUtil.getUserIdFromRequest());
        return this.findAllSales(saleFilter);

    }

    public SaleDTO findSale(Long id){
        Sale sale = saleService.findSale(id);
        return saleMapper.map(sale);
    }

    public SaleDTO findSaleForUser(Long id){
        Sale sale = saleService.getSaleForUser(id,GeneralUtil.getUserIdFromRequest());
        return saleMapper.map(sale);
    }

    public SaleDTO saveSale(SaleDTO saleDTO){
        Sale sale = saleMapper.reverseMap(saleDTO);
        //
        return saveAndMap(sale);
    }

    public SaleDTO purchaseSale(SaleDTO saleDTO){
        saleDTO.setUserId(GeneralUtil.getUserIdFromRequest());
        return this.saveSale(saleDTO);

    }

    public void deleteSale(Long id){
        saleService.deleteSale(id);
    }

    public void cancelSale(Long id){
        Sale sale = saleService.getSaleForUser(id,GeneralUtil.getUserIdFromRequest());
        sale.setSuspended(true);
        saleService.saveSale(sale);
    }

    public SaleDTO saveAndMap(Sale sale){
        Sale savedSale = saleService.saveSale(sale);
        return saleMapper.map(savedSale);
    }

}
