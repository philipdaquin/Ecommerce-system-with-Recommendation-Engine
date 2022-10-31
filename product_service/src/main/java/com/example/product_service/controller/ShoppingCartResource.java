package com.example.product_service.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

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

import com.example.product_service.Security.Utils;
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

    /**
     * {@code PUT /shop-cart/add-product/:id} : Add a product o active shopping cart of current User
     * 
     * @param id the id of the product to add 
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingcart,
     *          or with status {@code 400 (Bad Request)} if the shopping cart is not valid 
     *          or with status {@code 500 (Internal Server Error)} if the shopping car couldnt updated
     * @throws EntityNotFoundException if the product is not found 
     */
    public ResponseEntity<ShoppingCart> addProductToCart(@PathVariable Long id)  throws EntityNotFoundException { 
        log.debug("Request to add product in the shopping cart");
        String user = Utils.getCurrentUser().orElseThrow(() -> new EntityNotFoundException("Unable to get User!"));
        ShoppingCart result = shoppingCartService.addProductForUser(id, user);

        return ResponseEntity
            .ok()
            .header(ENTITY, result.getId().toString())
            .body(result);
    }   
}
