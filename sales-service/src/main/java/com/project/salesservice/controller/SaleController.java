package com.project.salesservice.controller;

import com.project.salesservice.dto.SaleDTO;
import com.project.salesservice.dto.SaleFilterDTO;
import com.project.salesservice.facade.SaleFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sales")
public class SaleController {

    private final SaleFacade saleFacade;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SaleDTO> getAllSales(@ModelAttribute SaleFilterDTO saleFilter){
        return saleFacade.listSales(saleFilter);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SaleDTO getSale(@PathVariable("id") Long id){
        return saleFacade.findSale(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleDTO saveSale(@RequestBody SaleDTO saleDTO){
        return saleFacade.purchaseSale(saleDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSale(@PathVariable("id") Long id){
        saleFacade.deleteSale(id);
    }

}
