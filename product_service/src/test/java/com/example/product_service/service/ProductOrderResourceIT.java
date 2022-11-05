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
import com.example.product_service.model.Product;
import com.example.product_service.model.ProductOrder;
import com.example.product_service.model.ShoppingCart;
import com.example.product_service.repository.ProductOrderRepository;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = "ROLE_ADMIN", password = "admin")
public class ProductOrderResourceIT {
    
    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final BigDecimal DEFAULT_TOTALPRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTALPRICE = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/product-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));


    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc restMockMvc;

    private ProductOrder productOrder;


    /**
     * 
     * @param entityManager
     * @return
     */
    public static ProductOrder createProductOrder(EntityManager entityManager) { 
        ProductOrder productOrder = new ProductOrder();
        productOrder.setQuantity(DEFAULT_QUANTITY);
        productOrder.setTotalPrice(DEFAULT_TOTALPRICE);

        Product product;
        if (TestUtils.findAll(entityManager, Product.class).isEmpty()) { 
            product = ProductResourceTest.createProductEntity(entityManager);
            entityManager.persist(product);
            entityManager.flush();
        } else { 
            product = TestUtils.findAll(entityManager, Product.class).get(0);
        }

        productOrder.setProduct(product);

        ShoppingCart shoppingCart;
        if (TestUtils.findAll(entityManager, ShoppingCart.class).isEmpty()) { 
            shoppingCart = ShoppingCartTest.createShoppingCartEntity(entityManager);
            entityManager.persist(shoppingCart);
            entityManager.flush();
        } else { 
            shoppingCart = TestUtils.findAll(entityManager, ShoppingCart.class).get(0);
        }

        productOrder.setCart(shoppingCart);
        return productOrder;
    }
    /**
     * 
     * @param entityManager
     * @return
     */
    public static ProductOrder createUpdatedProductOrder(EntityManager entityManager) { 
        ProductOrder productOrder = new ProductOrder();
        productOrder.setQuantity(UPDATED_QUANTITY);
        productOrder.setTotalPrice(UPDATED_TOTALPRICE);

        Product product;
        if (TestUtils.findAll(entityManager, Product.class).isEmpty()) { 
            product = ProductResourceTest.createProductEntity(entityManager);
            entityManager.persist(product);
            entityManager.flush();
        } else { 
            product = TestUtils.findAll(entityManager, Product.class).get(0);
        }

        productOrder.setProduct(product);

        ShoppingCart shoppingCart;
        if (TestUtils.findAll(entityManager, ShoppingCart.class).isEmpty()) { 
            shoppingCart = ShoppingCartTest.createUpdatedShoppingCartEntity(entityManager);
            entityManager.persist(shoppingCart);
            entityManager.flush();
        } else { 
            shoppingCart = TestUtils.findAll(entityManager, ShoppingCart.class).get(0);
        }

        productOrder.setCart(shoppingCart);
        return productOrder;
    }

    /**
     * 
     */
    @BeforeEach
    public void init() { 
        productOrder = createProductOrder(entityManager);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    @Transactional
    void createProductOrder() throws Exception { 
        int databaseSize = productOrderRepository.findAll().size();

        restMockMvc.perform(
            post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.convertObjectToJsonBytes(productOrder))
        ).andExpect(status().isCreated());

        // Validate that the product is in the databse

        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        
        assertThat(productOrderList).hasSize(databaseSize);
        ProductOrder testproductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testproductOrder.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testproductOrder.getTotalPrice()).isEqualTo(DEFAULT_TOTALPRICE);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    @Transactional
    void createdProductOrderWithExistingId() throws Exception { 
        productOrder.setId(1L);

        int databaseSize = productOrderRepository.findAll().size();

        restMockMvc.perform(
            post(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.convertObjectToJsonBytes(productOrder))
        ).andExpect(status().isBadRequest());

        // Check if that item has been added in the database
        var productList = productOrderRepository.findAll();
        assertThat(productList).hasSize(databaseSize);
    }
}
