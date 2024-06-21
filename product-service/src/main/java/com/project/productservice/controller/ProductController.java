package com.project.productservice.controller;

import com.project.productservice.dto.ProductDTO;
import com.project.productservice.entity.Product;
import com.project.productservice.facade.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductFacade productFacade;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String sortBy){

        return productFacade.findAllProducts(PageRequest.of(page,size, Sort.by(sortBy)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProduct(@PathVariable("id") Long id){

        return productFacade.findProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductDTO saveProduct(@RequestBody ProductDTO productDTO){

        return productFacade.saveProduct(productDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductDTO updateProduct(@PathVariable("id") Long id,@RequestBody ProductDTO  productDTO){

        return productFacade.updateProduct(id,productDTO);
    }

    @PutMapping("/decrease/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_AUTOMATION')")
    public void decreasePurchasedAmount(@PathVariable("id") Long id, @RequestParam Long count){

        productFacade.decreasePurchase(id,count);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteEvent(@PathVariable("id") Long id){

        productFacade.deleteProduct(id);
    }




}
