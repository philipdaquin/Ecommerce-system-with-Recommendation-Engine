package com.example.product_service.service;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    
    /**
     * Creates a product in the database
     * @param product
     * @return the persisted entity
     */
    public Product createProduct(Product product) { 
        log.debug("Receive request to save Product: {}", product);
        return productRepository.save(product);
    }

    /**
     * Gets all the product 
     * @param pageable
     * @return the list of products 
     */
    @Transactional(readOnly = true)
    public Page<Product> findAllProduct(Pageable pageable) { 
        log.debug("Receive request to all Products");
        
        return productRepository.findAll(pageable);
    }

    /**
     * Get product by id
     * @param id of the product
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Product> getProduct(Long id) { 
        log.debug("Receive request to get Product: {}", id);
        return productRepository.findById(id);
    }

    /**
     * Delete the product by id
     * @param id of the entity
     */
    public void deleteProduct(Long id) { 
        log.debug("Receive request to delete Product: {}", id);
        productRepository.deleteById(id);
    }

    /**
     * Update a product 
     * @param product the entity to update
     * @return the persisted entity in the database
     */
    public Optional<Product> updateProduct(Product product) { 
        // Product productFromDb = productRepository.findById(product.getId())
        //     .orElseThrow(() -> new IllegalStateException("Unable to get the product"));
        // if (product.getName() != null) productFromDb.setName(product.getName());
        
        return productRepository.findById(product.getId())
            .map(productFromDb -> {
                
                if (product.getName() != null) productFromDb.setName(product.getName());
                if (product.getDescription() != null) productFromDb.setDescription(product.getDescription());
                if (product.getBrandName() != null)   productFromDb.setBrandName(product.getBrandName());
                if (product.getPricePerUnit() != null) productFromDb.setPricePerUnit(product.getPricePerUnit());
                if (product.getImage() != null) productFromDb.setImage(product.getImage());
                if (product.getContentType() != null) productFromDb.setContentType(product.getContentType());
                if (product.getItemSize() != null) productFromDb.setItemSize(product.getItemSize());
                if (product.getProductCategory() != null) productFromDb.setProductCategory(product.getProductCategory());

                return productFromDb;
            }
        ).map(productRepository::save);
    }
}
