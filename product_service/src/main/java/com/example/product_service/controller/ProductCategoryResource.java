package com.example.product_service.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.product_service.model.ProductCategory;
import com.example.product_service.repository.ProductCategoryRepository;
import com.example.product_service.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = {"/api"}, method = {
    RequestMethod.GET, 
    RequestMethod.DELETE, 
    RequestMethod.POST,
    RequestMethod.PUT,
    RequestMethod.PATCH
})
@RequiredArgsConstructor
public class ProductCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryResource.class);

    private static final String ENTITY = "product-category";

    private final ProductCategoryService productCategoryService;

    private final ProductCategoryRepository productCategoryRepository;
    
    // @Value("${application_name}")
    // private String applicationName;

    /**
     * {@code POST /product-category} : Create a new Product Category
     * @param id the id of the productCategory to save 
     * @param productCategory the entity to create
     * @return the {@link ResponseEntity} with the status {@code 201} and with the body product Category or with the 
     *         status {@code 400 (bad request)} if the producct category has already an id
     *          status {@code 500 (Internal Server Error) if the product categoru couldn't be udpated}
     * @throws URISyntaxException if the location URI syntax is incorrect
     */
    @PostMapping("/product-categories")
    public ResponseEntity<ProductCategory> createProductCategoryResource(
        @Valid @RequestBody ProductCategory productCategory
    ) throws URISyntaxException { 

        log.debug("Received request to create Product Category : {}", productCategory);
        if (productCategory.getId() != null)  log.error("A new ProductCategory cannot have an id!");
        ProductCategory result = productCategoryService.createCategory(productCategory);
        
        return ResponseEntity
            .created(new URI("/api/product-categories/" + result.getId()))
            .header("product-service", ENTITY, productCategory.getId().toString())
            .body(result);
    }

    /**
     * 
     * @param productCategory
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PatchMapping(value = "/product-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductCategory> partialUpdateProductCategoryResource(
        @Valid @RequestBody final ProductCategory productCategory, 
        @PathVariable(value = "id", required = false) final Long id
    ) throws URISyntaxException { 
        
        if (productCategory.getId() == null) log.error("Invalid Id");
        if (!Objects.equals(productCategory.getId(), id)) log.error("Invalid Id");
        if (productCategoryRepository.existsById(id)) log.error("Entity not found!");

        Optional<ProductCategory> result = productCategoryService.updateCategory(productCategory);
        if (!result.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(result.get());
    }
    
    /**
     * {@code GET / product-categories} : get all the product categories
     * @param pageable the pagination information 
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productCategories in body
     */
    @GetMapping("/product-categories")
    public ResponseEntity<List<ProductCategory>>  getAllProductCategoriesResource(Pageable pageable) { 
        log.debug("Received request to get a page of ProductCategories");
        Page<ProductCategory> page = productCategoryService.getAllCategory(pageable);
        return ResponseEntity
            .ok()
            .header(ENTITY, page.toString())
            .body(page.getContent());
    }

    /**
     * {@code GET /product-category/{id} : get the "id" product} 
     * @param id of the product category 
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the product category,
     * or with the body the product category, or with status {@code 404 (NOT FOUND)
     */
    @GetMapping("/product-categories/{id}/")
    public ResponseEntity<ProductCategory> getProductCategory(@PathVariable Long id) {
        log.debug("Received request to get Product Category {} ", id);
        Optional<ProductCategory> productCategory = productCategoryService.getCategory(id);
        if (!productCategory.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(productCategory.get());
    }

    /**
     * {@code DELETE / product-categories/:id} : delete the "id" product category
     * @param id of the product Category to delete
     * @return the {@link ReponseEntity} with status {@code @204 (NO Content)} 
     */
    @DeleteMapping("/product-categories/{id}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Long id) { 
        log.debug("Received request to delete Product Categoryw : {} ", id);

        productCategoryService.deleteCategory(id);
        return ResponseEntity
            .noContent()
            .header(ENTITY, id.toString())
            .build();
    }
}
