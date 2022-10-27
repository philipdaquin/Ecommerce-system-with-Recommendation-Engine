package com.example.product_service.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_service.Security.AuthoritiesConstants;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = {"/api"}, method = {
    RequestMethod.GET, 
    RequestMethod.DELETE, 
    RequestMethod.POST
})
@AllArgsConstructor
public class ProductResource {
    
    private static final String ENTITY = "product";

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private final ProductService productService;
    
    private final ProductRepository productRepository;

    @Value("${application_name}")
    private final String applicationName;

    /**
     * {@code Get /products/:id : get the "id" product}
     * @param id the id of the product 
     * @return return the }@link ResponseEntity} with status {@code 200 (OK)} and with body the 
     * product, or with status {@code 404 (NOT FOUND)}
     */
    @GetMapping(value = "/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) { 
        log.debug("Get Request to get Product: {}", id);

        Optional<Product> product = productService.getProduct(id);
        if (!product.isPresent()) {
            log.info("Unable to get product from Database");
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(product.get());
    }

    /**
     * {@code DELETE  /product/:id} delete the "id" product
     * @param id of the product to be delete 
     * @return return the {@link ResponseEntity} with status {@code 404 (No Content)}
     */
    @DeleteMapping("/product/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) { 
        log.debug("Receive request to delete Product: {}", id);
        
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
