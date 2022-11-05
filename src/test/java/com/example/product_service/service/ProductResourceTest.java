package com.example.product_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.product_service.IntegrationTest;
import com.example.product_service.controller.ProductCategoryResource;
import com.example.product_service.model.Product;
import com.example.product_service.model.ProductCategory;
import com.example.product_service.model.enumeration.Size;
import com.example.product_service.repository.ProductRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username = "testadmin", authorities = "ROLE_ADMIN", password = "testadmin")
public class ProductResourceTest {
    
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final Size DEFAULT_ITEM_SIZE = Size.S;
    private static final Size UPDATED_ITEM_SIZE = Size.M;

    private static final byte[] DEFAULT_IMAGE = new byte[100];
    private static final byte[] UPDATED_IMAGE = new byte[100];
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));



    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc resMockMvc;

    @Autowired
    private EntityManager entityManager;

    private Product product;

    /**
     * Creates a product entity for this test
     * @param eManager
     * @return
     */
    public static Product createProductEntity(EntityManager eManager){ 
        Product product = new Product();
        product.setName(DEFAULT_NAME);
        product.setDescription(DEFAULT_DESCRIPTION);
        product.setPricePerUnit(DEFAULT_PRICE);
        product.setItemSize(DEFAULT_ITEM_SIZE);
        product.setContentType(DEFAULT_IMAGE_CONTENT_TYPE);

        ProductCategory productCategory;
        if (TestUtils.findAll(eManager, Product.class).isEmpty()) { 
            productCategory = ProductCategoryResouceIT.createProductCategory(eManager);
            eManager.persist(productCategory);
            eManager.flush();
        } else { 
            productCategory = TestUtils.findAll(eManager, ProductCategory.class).get(0);
        }

        product.setProductCategory(productCategory);

        return product;
    }

    public static Product updateProductEntity(EntityManager eManager) { 
        Product product = new Product();
        product.setName(UPDATED_NAME);
        product.setDescription(UPDATED_DESCRIPTION);
        product.setPricePerUnit(UPDATED_PRICE);
        product.setItemSize(UPDATED_ITEM_SIZE);
        product.setContentType(UPDATED_IMAGE_CONTENT_TYPE);

        ProductCategory productCategory;
        if (TestUtils.findAll(eManager, Product.class).isEmpty()) { 
            productCategory = ProductCategoryResouceIT.updateProductCategory(eManager);
            eManager.persist(productCategory);
            eManager.flush();
        } else { 
            productCategory = TestUtils.findAll(eManager, ProductCategory.class).get(0);
        }

        product.setProductCategory(productCategory);

        return product;
    }
    @BeforeEach
    public void init() { 
        product = createProductEntity(entityManager);
    }

    @Test   
    @Transactional
    void createProduct() throws Exception {
        int databaseSize = productRepository.findAll().size();

        resMockMvc.perform(
            post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.convertObjectToJsonBytes(product))
        ).andExpect(status().isCreated());
        
        List<Product> productList = productRepository.findAll();

        assertThat(productList).hasSize(databaseSize);

        Product testProduct = productList.get(productList.size() - 1);

        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getPricePerUnit()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getItemSize()).isEqualTo(DEFAULT_ITEM_SIZE);
        assertThat(testProduct.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProduct.getContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception { 
        product.setId(1L);

        int databaseSize = productRepository.findAll().size();

        resMockMvc.perform(
            post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.convertObjectToJsonBytes(product))
        ).andExpect(status().isCreated());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSize);
    }

    @Test
    @Transactional
    void getProduct() throws Exception { 
        productRepository.saveAndFlush(product);
        
        resMockMvc.perform(get(ENTITY_API_URL + "?sort=id, desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].image_content_type").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].item_size").value(hasItem(DEFAULT_ITEM_SIZE)));
    }
}
