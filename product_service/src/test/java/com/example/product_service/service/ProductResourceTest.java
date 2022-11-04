package com.example.product_service.service;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.product_service.IntegrationTest;
import com.example.product_service.model.Product;
import com.example.product_service.model.enumeration.Size;
import com.example.product_service.repository.ProductRepository;

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
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    private Product product;

    public static Product createProductEntity(EntityManager eManager){ 
        Product product = new Product();
        product.setName(DEFAULT_NAME);
        product.setDescription(DEFAULT_DESCRIPTION);
        product.setPricePerUnit(DEFAULT_PRICE);
        product.setItemSize(DEFAULT_ITEM_SIZE);

        return product;

    }
    
}
