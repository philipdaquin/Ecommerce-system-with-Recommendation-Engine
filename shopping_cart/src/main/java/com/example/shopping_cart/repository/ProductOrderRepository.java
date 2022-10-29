package com.example.shopping_cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shopping_cart.models.ProductOrder;

@SuppressWarnings("unused`")
@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    
}
