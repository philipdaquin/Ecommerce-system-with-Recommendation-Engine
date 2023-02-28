package com.example.recommender_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.recommender_service.models.ProductViewHistory;

@Repository
public interface ProductViewHistoryRepository extends JpaRepository<ProductViewHistory, Integer> { 
    
    @Query(value = "SELECT productId from productViewHistory WHERE userId = ?1 ORDER BY clickTimestamp DESC LIMIT 10", nativeQuery = true)
    List<Integer> getLastViewedProduct(Integer userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM productViewHistory WHERE userId = ?1 AND productId = ?2", nativeQuery = true)
    void deleteProductViewHistory(Integer userId, Integer productId);
}
