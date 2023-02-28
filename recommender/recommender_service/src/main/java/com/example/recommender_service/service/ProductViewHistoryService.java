package com.example.recommender_service.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.recommender_service.models.ProductViewHistory;
import com.example.recommender_service.models.ProductViewResponse;
import com.example.recommender_service.repository.ProductViewHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductViewHistoryService {
    
    private final Logger log = LoggerFactory.getLogger(ProductService.class); 

    private ProductViewHistoryRepository productViewHistoryRepository;

    /**
     * Save a product 
     * @param productViewHistory the entity  to save 
     * @return the persisted entity 
     */
    public ProductViewHistory save(ProductViewHistory productViewHistory) { 
        return productViewHistoryRepository.save(productViewHistory);
    }

    /**
     * Get one product view history by product
     * @param productId of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ProductViewHistory> findOne(Integer productId) { 
        return productViewHistoryRepository.findById(productId);
    } 

    /**
     * Get all product view history 
     * @param pageable the pagination information 
     * @return the list of entities 
     */
    @Transactional(readOnly = true)
    public Page<ProductViewHistory> findAll(Pageable pageable) { 
        return productViewHistoryRepository.findAll(pageable);
    } 

    /**
     * Delete the product view history by id 
     * @param productId the id of the entity 
     * @return boolean, where True if the entity is deleted 
     */
    public boolean delete(Integer productId) { 
        try {
            productViewHistoryRepository.deleteById(productId);
            return true;
        } catch (Exception e) { 
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Delete product history based on the user 
     * @param userId of the user
     * @param productId the id of the product view history 
     * @return boolean 
     */
    public boolean deleteProductViewHistory(Integer userId, Integer productId) { 
        try {   
            productViewHistoryRepository.deleteProductViewHistory(userId, productId);
            return true;
        } catch (Exception e ) { 
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Get the most recent viewed product 
     * @param userId the id of the user
     * @return the list of product view history ids 
     */
    public List<Integer> getLastViewedProduct(Integer userId) { 
        return productViewHistoryRepository.getLastViewedProduct(userId);
    }

}
