package com.example.shopping_cart.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping_cart.models.ProductOrder;
import com.example.shopping_cart.repository.ShoppingCartRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ShoppingCartService {
    
    private final Logger log = LoggerFactory.getLogger(ShoppingCartService.class);

    private final ShoppingCartRepository repository;
    
    

}
