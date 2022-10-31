package com.example.product_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_service.model.ShoppingCart;
import com.example.product_service.repository.ShoppingCartRepository;
import com.example.product_service.service.ShoppingCartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = {"/api"}, method = {
    RequestMethod.GET, 
    RequestMethod.DELETE, 
    RequestMethod.POST,
    RequestMethod.PUT
})
@RequiredArgsConstructor
public class ShoppingCartResource {
    
    private final Logger log = LoggerFactory.getLogger(ShoppingCartResource.class);
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final String ENTITY = "shopping_cart";

    /**
     * Gets all a list of shopping carts 
     * @return
     */
    @GetMapping("/shop-cart")
    // @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.)")
    public List<ShoppingCart> getAllShoppingCarts() { 
        return shoppingCartService.getAll();
    }

    /**
     * {@code DELETE /shop-cart/:id} : delete the "id" shopping cart 
     * 
     * @param id the id of the shopping cart to delete
     * @return the {@link ResponseEntity} with status {@code 204 (NO CONTENT)
     */
    @DeleteMapping("/shop-cart/{id}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Long id) { 
        shoppingCartRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .header(ENTITY, id.toString())
            .build();    
    }   
}
