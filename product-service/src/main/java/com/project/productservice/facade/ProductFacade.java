package com.project.productservice.facade;

import com.project.productservice.dto.ProductDTO;
import com.project.productservice.entity.Product;
import com.project.productservice.mapper.ProductMapper;
import com.project.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public List<ProductDTO> findAllProducts(Pageable pageable){
        return productService.findAllProducts(pageable)
                .getContent()
                .stream()
                .map(productMapper::map)
                .toList();
    }

    public ProductDTO findProduct(Long id){
        Product product = productService.findProduct(id);
        return productMapper.map(product);
    }

    public ProductDTO saveProduct(ProductDTO productDTO){
        Product product = productMapper.reverseMap(productDTO);
        return saveAndMap(product);
    }

    public ProductDTO updateProduct(Long id,ProductDTO productDTO){
        Product savingProduct = productMapper.reverseMap(productDTO);
        savingProduct.setId(id);
        return productMapper.map(savingProduct);

    }

    public void deleteProduct(Long id){
        productService.deleteProduct(id);
    }

    public void decreasePurchase(Long id,Long count){
        Product product = productService.findProduct(id);
        long leftOver = product.getCapacity() - count;
        if (leftOver <= 0){
            throw new RuntimeException("Not enough capacity!");
        }
        product.setCapacity(leftOver);
        productService.saveProduct(product);
    }

    private ProductDTO saveAndMap(Product product){
        Product savedProduct = productService.saveProduct(product);
        return productMapper.map(savedProduct);
    }
}
