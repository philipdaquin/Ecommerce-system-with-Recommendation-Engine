package com.example.shopping_cart.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopping_cart.models.ProductOrder;
import com.example.shopping_cart.repository.ProductOrderRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ProductOrderService {
    
    private final Logger log = LoggerFactory.getLogger(ProductOrderService.class);
    private final ProductOrderRepository orderRepository;

    /**
     * Saves the entity into the database
     * @param productOrder the entity to save
     * @return the persisted entity
     */
    public ProductOrder save(ProductOrder productOrder) { 
        log.debug("Request to save ProductOrder: {}", productOrder);
        return orderRepository.save(productOrder);
    }

    /**
     * Partially update a product order
     * @param productOrder the entity to update partially 
     * @return the persisted entity
     */
    public Optional<ProductOrder> partialUpdate(ProductOrder productOrder) { 
        log.debug("Request to update product order: {}", productOrder);

        return orderRepository.findById(productOrder.getId())
            .map(existingOrder -> {
                if (productOrder.getQuantity() != null) productOrder.setQuantity(productOrder.getQuantity());
                if (productOrder.getTotalPrice() != null) productOrder.setTotalPrice(productOrder.getTotalPrice());
                return existingOrder;
            }).map(orderRepository::save);
    }

    /**
     * Gets all product orders
     * @return a list of product orders
     */
    public List<ProductOrder> getAll() {
        log.debug("Request to get all product order");
        return orderRepository.findAll();
    }

    /**
     * Finds one product order by id 
     * @param id of the entity 
     * @return the entity
     */
    public Optional<ProductOrder> findOne(Long id) { 
        log.debug("Request to get product by id: {}", id);

        return orderRepository.findById(id);
    }

    /**
     * Deletes product order by id 
     * @param id of the product order
     */
    public void deleteOne(Long id) {
        log.debug("Request to delete product order: {}", id);

        orderRepository.deleteById(id);
    }

}
