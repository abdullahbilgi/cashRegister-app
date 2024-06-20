package com.project.productservice.service;


import com.project.productservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<Product> findAllEvents(Pageable pageable);

    Product findProduct(Long id);

    Product saveProduct(Product product);

    void deleteProduct(Long id);

}
