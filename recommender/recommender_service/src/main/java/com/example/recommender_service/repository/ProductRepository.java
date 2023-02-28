package com.example.recommender_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.recommender_service.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
}
