package com.example.shopping_cart.service;


import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping_cart.models.ShoppingCart;
import com.example.shopping_cart.repository.ShoppingCartRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ShoppingCartService {
    
    private final Logger log = LoggerFactory.getLogger(ShoppingCartService.class);

    private final ShoppingCartRepository shoppingCartRepository;

    private final ProductOrderService productOrderService;
    

    /**
     * 
     * @param id
     * @return
     */
    public Optional<ShoppingCart> findOne(Long id) { 
        return shoppingCartRepository.findById(id);
    }

    /**
     * 
     * @param id
     */
    public void deleteOne(Long id) { 
        shoppingCartRepository.deleteById(id);
    }

    /**
     * 
     * @return
     */
    public List<ShoppingCart> getAll() { 
        return shoppingCartRepository.findAll();
    }

    /**
     * 
     * @param shoppingCart
     * @return
     */
    public ShoppingCart save(ShoppingCart shoppingCart) { 
        return shoppingCartRepository.save(shoppingCart);
    }

    /**
     * 
     * @param shoppingCart
     * @return
     */
    public Optional<ShoppingCart> partialUpdate(ShoppingCart shoppingCart) {
        return shoppingCartRepository.findById(shoppingCart.getId())
            .map(existingCart -> {
                
                if (shoppingCart.getOrderStatus() != null) existingCart.setOrderStatus(shoppingCart.getOrderStatus());
                if (shoppingCart.getTotalPrice() != null) existingCart.setTotalPrice(shoppingCart.getTotalPrice());
                if (shoppingCart.getPlacedDate() != null) existingCart.setPlacedDate(shoppingCart.getPlacedDate());
                if (shoppingCart.getPaymentMethod() != null) existingCart.setPaymentMethod(shoppingCart.getPaymentMethod());
                if (shoppingCart.getPaymentReference() != null) existingCart.setPaymentReference(shoppingCart.getPaymentReference());
                if (shoppingCart.getPaymentModificationReference() != null) existingCart.setPaymentModificationReference(shoppingCart.getPaymentModificationReference());

                return existingCart;
            }).map(shoppingCartRepository::save);
    }

}
