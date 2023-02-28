package com.example.recommender_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.recommender_service.models.BestSeller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class BestSellerService {
    
    /**
     * A persistence context is a set of entity instances in which for any persistent entity
     * identity there is a unique entity instance. 
     * Within the persistence context, the entity instances and their lifecycle are managed 
     */
    @PersistenceContext
    private EntityManager entityManager;

    private static final String QueryString = 
        "SELECT productId FROM bestSellerProduct WHERE categoryId IN" + 
        "(SELECT categoryId FROM productViewHistory P INNER JOIN products AS P ON P.productId WHERE userId = ? LIMIT3)";
    
    private static final String CategoryQuery = "SELECT productId FROM bestSellerProduct ORDER BY saleAmoutn DESC LIMIT 10";
    
    /**
     * Get product recommendation
     * @param userId of the targeted user
     * @return BestSeller items 
     */
    public BestSeller getProductRecommendation(Integer userId) { 
        Query specificCategoryQuery = entityManager.createNativeQuery(QueryString);
        specificCategoryQuery.setParameter(1, userId);

        List<BestSeller> specificBestSeller = specificCategoryQuery.getResultList();

        BestSeller bestSeller = new BestSeller();

        bestSeller.setUserId(userId);
        
        if (!specificBestSeller.isEmpty()) {
            for (BestSeller seller : specificBestSeller) 
                bestSeller.getProductIds().addAll(seller.getProductIds());
            bestSeller.setType("personalised recommendation");
        } else  { 
            Query category = entityManager.createNamedQuery(CategoryQuery);
            List<BestSeller> generalBestSellers= category.getResultList();
            
            for (BestSeller seller : generalBestSellers) { 
                bestSeller.getProductIds().addAll(seller.getProductIds());
            }
            bestSeller.setType("general recommendations");

        }

        return bestSeller;
    }
}
