package com.example.product_service.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.product_service.Security.Utils;
import com.example.product_service.model.ShoppingCart;
import com.example.product_service.model.enumeration.OrderStatus;
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
     * {@code DELETE /shop-cart/remove-order/:id} : Remove a product order from active from shopping cart of current user
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    public ResponseEntity<ShoppingCart> removeShoppingCart(final Long id ) throws EntityNotFoundException { 
        log.debug("Request to remove shopping cart!");

        String user = Utils.getCurrentUser().orElseThrow(() -> new EntityNotFoundException("Unable to find the current user"));
        ShoppingCart result = shoppingCartService.removeProductFromUser(id, user);
        return ResponseEntity
            .ok()
            .header(ENTITY, id.toString())
            .body(result);
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

    /**
     * 
     * @return
     * @throws EntityNotFoundException
     */
    public ResponseEntity<ShoppingCart> getActiveCartByUser() throws EntityNotFoundException { 
        String user = Utils.getCurrentUser().orElseThrow(() -> new EntityNotFoundException("Unable to get User!"));
        ShoppingCart result = shoppingCartService.findActiveCartByUser(user);
        return ResponseEntity
            .ok()
            .header(ENTITY, result.getId().toString())
            .body(result);
    }

    /**
     * 
     * @return
     */
    public ResponseEntity<List<ShoppingCart>> getAllCartsByUser() { 
        String user = Utils.getCurrentUser().orElseThrow(() -> new EntityNotFoundException("Unable to get User!"));
        List<ShoppingCart> result = shoppingCartService.findCartsByUser(user);

        return ResponseEntity
            .ok()
            .body(result);
    }

    public ResponseEntity<ShoppingCart> closeShoppingCart(
        @RequestParam String paymentType, 
        @RequestParam String paymentRef,
        @RequestParam OrderStatus status
    ) throws EntityNotFoundException{
        String user = Utils.getCurrentUser().orElseThrow(() -> new EntityNotFoundException("Unable to find user"));

        ShoppingCart result = shoppingCartService.updateCartWithPaymentByUserName(user, paymentType, paymentRef, status);
        return ResponseEntity
            .ok()
            .header(ENTITY, result.getId().toString())
            .body(result);
    }



}
