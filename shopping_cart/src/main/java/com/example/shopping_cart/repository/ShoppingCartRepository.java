package com.example.shopping_cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shopping_cart.models.ShoppingCart;

@SuppressWarnings("unused")
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    
}
