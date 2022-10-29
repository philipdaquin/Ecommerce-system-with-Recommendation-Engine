package com.example.product_service.controller;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    RequestMethod.POST,
    RequestMethod.PUT
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
     * @return return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the 
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
    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) { 
        log.debug("Receive request to delete Product: {}", id);
        
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code PATCH /producs:id} : Partial updates given fields of an existing product, field will 
     * ignore if it null 
     * @param id of the product 
     * @param product 
     * @return 
     * @throws URISyntaxException if the location URI syntax is incorrect
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Product product
    ) throws URISyntaxException { 
        log.debug("Receive request to update Product {}, {}", id, product);
        
        if (!Objects.equals(id, product.getId())) log.error("Id is the same as Product Id");
        if (product.getId() == null) log.error("Error: Not found");
        if (!productRepository.existsById(id)) log.error("Exception: Not found in the database!"); 
        
        Optional<Product> result = productService.updateProduct(product);
        return ResponseEntity.ok(result.get());
    }

    /**
     * {@code POST /producsts}  : Create a new product 
     * @param product the product to create 
     * @return return Status {@code status 201 (created) and with the body the new product }, or status 
     * {@code 400 (Bad Request )} if the product has already an Id 
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/products")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Product> createProduct(
        @Valid @RequestBody Product product
    ) throws URISyntaxException {
        log.debug("Received request to create Product : {}", product);
        Product result = productService.createProduct(product);

        return ResponseEntity
            .created(new URI("/api/products/" + result.getId()))
            .header(applicationName, ENTITY, product.getId().toString())
            .body(result);
    }

    /**
     * {@code GET / product} : Get a list of products 
     * @param pageable the pagination information 
     * @return the {@link} ResponseEntity} with status {@code 200 (OK)} and the list of products
     */
    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProducts(Pageable pageable) {
        log.debug("Request to get a page of Product");
        Page<Product> page = productService.findAllProduct(pageable);
        return ResponseEntity
            .ok()
            .header(ENTITY, page.toString())
            .body(page.getContent());
    }
}
