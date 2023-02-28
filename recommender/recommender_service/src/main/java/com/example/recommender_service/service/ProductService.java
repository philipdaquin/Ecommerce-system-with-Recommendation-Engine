package com.example.recommender_service.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.recommender_service.models.Product;
import com.example.recommender_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class); 

    private ProductRepository productRepository;

    
    public void delete(Integer productId) { 
        productRepository.deleteById(productId);
    } 

    /**
     * Get one product by id
     * @param productId of the entity
     * @return the entity
     */
    public Optional<Product> findOne(Integer productId) { 
        return productRepository.findById(productId);
    }
}
