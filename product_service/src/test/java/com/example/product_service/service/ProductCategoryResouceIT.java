package com.example.product_service.service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.product_service.IntegrationTest;
import com.example.product_service.model.ProductCategory;
import com.example.product_service.repository.ProductCategoryRepository;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username = "testadmin", authorities = "ROLE_ADMIN", password = "testadmin")
public class ProductCategoryResouceIT {
    
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";
    
    private static final String ENTITY_API_URL = "/api/product-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));


    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc restMockMvc;

    private ProductCategory productCategory;


    /**
     * 
     * @param entityManager
     * @return
     */
    public static ProductCategory createProductCategory(EntityManager entityManager) { 
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(DEFAULT_NAME);
        productCategory.setDescription(DEFAULT_DESCRIPTION);

        return productCategory;
    } 

    /**
     * 
     * @param entityManager
     * @return
     */
    public static ProductCategory updateProductCategory(EntityManager entityManager) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(UPDATED_NAME);
        productCategory.setDescription(UPDATED_DESCRIPTION);

        return productCategory;
    }

    @BeforeEach
    public void init() { 
        productCategory = createProductCategory(entityManager);
    }
    

}
