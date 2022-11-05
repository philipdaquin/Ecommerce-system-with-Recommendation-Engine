package com.example.product_service.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.assertThat;
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
import com.example.product_service.model.Product;
import com.example.product_service.model.ProductCategory;
import com.example.product_service.repository.ProductCategoryRepository;
import com.example.product_service.repository.ProductRepository;

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


    private ProductRepository productRepository;

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
    
    /**
     * 
     * @throws Exception
     */
    @Test
    @Transactional
    void createProductCategory() throws Exception {
        // the number of elements in the list
        int databaseSize = productCategoryRepository.findAll().size();


        restMockMvc.perform(
            post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.convertObjectToJsonBytes(productCategory))
        ).andExpect(status().isCreated());

        List<Product> productList = productRepository.findAll();
        
        assertEquals(databaseSize, productList.size() + 1);
        
        Product testProduct = productList.get(databaseSize - 1);

        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    @Transactional
    void deleteProductCategory() throws Exception { 
        productCategoryRepository.saveAndFlush(productCategory);

        int databaseSize = productCategoryRepository.findAll().size();
        restMockMvc.perform(
            delete(ENTITY_API_URL_ID, productCategory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList.size()).isEqualTo(databaseSize - 1);
    }
    /**
     * 
     * @throws Exception
     */
    @Test
    @Transactional
    void putNewProductCategory() throws Exception  { 
        // initiliase the database
        productCategoryRepository.saveAndFlush(productCategory);
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Updte the product category 
        ProductCategory updatedProductCategory = productCategoryRepository
            .findById(productCategory.getId())
            .get();
        
        // Disconnect from session so that the updates on updatedProductCategory are not directly saved in db
        entityManager.detach(updatedProductCategory);

        updatedProductCategory.setName(UPDATED_NAME);
        updatedProductCategory.setDescription(UPDATED_DESCRIPTION);


        restMockMvc.perform(
            put(ENTITY_API_URL, updatedProductCategory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.convertObjectToJsonBytes(updatedProductCategory))
        ).andExpect(status().isOk());

        List<ProductCategory> productCategorieslist = productCategoryRepository.findAll();
        assertThat(productCategorieslist).hasSize(databaseSizeBeforeUpdate);
    }

    /**
     * Test partial updates for productCategory
     * @throws Exception
     */
    @Test
    @Transactional
    void partialUpdateProductCategoryWithPatch() throws Exception { 
        // intialise the database
        productCategoryRepository.saveAndFlush(productCategory);

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        ProductCategory updatedProductCategory = new ProductCategory();
        updatedProductCategory.setId(productCategory.getId());

        restMockMvc.perform(
            patch(ENTITY_API_URL, updatedProductCategory.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(updatedProductCategory))
        ).andExpect(status().isOk());

        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);

        ProductCategory testproductcategory = productCategoryList.get(databaseSizeBeforeUpdate - 1);

        assertThat(testproductcategory.getName()).isEqualTo(DEFAULT_NAME);

        assertThat(testproductcategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    @Transactional 
    void getNonExistentProductCategory() throws Exception  { 
        restMockMvc.perform(
            get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

   

}
