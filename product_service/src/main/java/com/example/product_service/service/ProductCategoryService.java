package com.example.product_service.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.product_service.model.ProductCategory;
import com.example.product_service.repository.ProductCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCategoryService {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryService.class);
    private final ProductCategoryRepository productCategoryRepository;

    /**
     * Creates a new product category 
     * @param productCategory
     * @return the persisted entity
     */
    public ProductCategory createProductCategory(ProductCategory productCategory) { 
        log.debug("Request to create new Product Category: {}", productCategory);
        return productCategoryRepository.save(productCategory);
    }

    /**
     * Deletes the product category by id
     * @param id of the product category
     */
    public void deleteProductCategory(Long id) { 
        log.debug("Request to delete Product Category: {} ", id);
        productCategoryRepository.deleteById(id);
    }
    
    /**
     * Gets product category by id
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<ProductCategory> getCategory(Long id) { 
        log.debug("Request to get Product CategoryL {}", id);
        return productCategoryRepository.findById(id);
    }

    /**
     * Gets all product categories
     * @param pageable the pagination information
     * @return the list of all product categories
     */
    public Page<ProductCategory> getAllCategory(Pageable pageable) { 
        log.debug("Request to get all ProductCategories: {}");
        return productCategoryRepository.findAll(pageable);
    }

    /**
     * Updates a product category
     * 
     * @param productCategory to update, nullable 
     * @return the persisted Product Category
     */
    public Optional<ProductCategory> updateCategory(ProductCategory productCategory) { 
        return productCategoryRepository.findById(productCategory.getId())
            .map(category -> { 
                if (productCategory.getName() != null) category.setName(productCategory.getName());
                if (productCategory.getDescription() != null) category.setDescription(productCategory.getDescription());
                return category;
            }).map(productCategoryRepository::save);
    } 
}
