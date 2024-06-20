package com.project.productservice.service;

import com.project.productservice.entity.Product;
import com.project.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findProduct(Long id) {

        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()){
            throw new RuntimeException("There's no event with id: " + id);
        }

        return productOpt.get();

    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);

    }
}
