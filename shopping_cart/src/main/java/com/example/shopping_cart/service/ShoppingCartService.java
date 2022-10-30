package com.example.shopping_cart.service;


import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping_cart.models.ShoppingCart;
import com.example.shopping_cart.models.enumeration.OrderStatus;
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
     * Get a shopping cart by id 
     * @param id of the shopping cart 
     * @return the entity 
     */
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> findOne(Long id) { 
        log.debug("Request to get a product by id: {}", id);

        return shoppingCartRepository.findById(id);
    }

    /**
     * Delete the shopping cart by id 
     * @param id of the shopping cart 
     */
    public void deleteOne(Long id) { 
        log.debug("Request to delete a shopping cart by id: {}", id);

        shoppingCartRepository.deleteById(id);
    }

    /**
     * Get all the shopping carts 
     * 
     * @return the list of shopping carts 
     */
    @Transactional(readOnly = true)
    public List<ShoppingCart> getAll() { 
        log.debug("Request to get all product order");

        return shoppingCartRepository.findAll();
    }

    /**
     * Saves a shopping cart 
     * @param shoppingCart
     * @return the persisted entity
     */
    public ShoppingCart save(ShoppingCart shoppingCart) { 
        log.debug("Request to get all product order");

        return shoppingCartRepository.save(shoppingCart);
    }

    /**
     * Partially update a shopping cart  
     * @param shoppingCart the entity to update partially 
     * @return the persisted entity 
     */
    public Optional<ShoppingCart> partialUpdate(ShoppingCart shoppingCart) {
        log.debug("Request to partially update a shopping cart: {} ", shoppingCart);

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

    @Transactional(readOnly = true)
    public List<ShoppingCart> findCartsByUser(final String user) { 
        return shoppingCartRepository.findAllByCustomerDetailsUserLoginAndStatusNot(user, OrderStatus.OPEN);
    }

    // public ShoppingCart findActiveCartByUser(String user) { 
    //     Optional<ShoppingCart> userCart = shoppingCartRepository.findFirstByCustomerDetailsUserLoginAndStatusOrderByIdAsc(user, OrderStatus.OPEN);
    //     ShoppingCart activeCart = userCart.orElseGet(() -> { 
    //         var customer = customer.
    //     })
    // }

    
}
