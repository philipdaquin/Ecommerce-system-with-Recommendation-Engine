package com.example.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.product_service.model.ProductOrder;



@SuppressWarnings("unused`")
@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    
}
