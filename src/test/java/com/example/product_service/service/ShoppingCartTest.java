package com.example.product_service.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.product_service.IntegrationTest;
import com.example.product_service.model.CustomerDetails;
import com.example.product_service.model.ShoppingCart;
import com.example.product_service.model.enumeration.OrderStatus;
import com.example.product_service.model.enumeration.PaymentMethod;
import com.example.product_service.repository.ShoppingCartRepository;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username = "testadmin", authorities = "ROLE_ADMIN", password = "testadmin")
public class ShoppingCartTest {
    
    private static final Instant DEFAULT_PLACED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLACED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.OPEN;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PAID;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);

    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.CREDIT_CARD;
    private static final PaymentMethod UPDATED_PAYMENT_METHOD = PaymentMethod.CASH;

    private static final String DEFAULT_PAYMENT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_MODIFICATION_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_MODIFICATION_REFERENCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shopping-carts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShoppingCartMockMvc;

    private ShoppingCart shoppingCart;

    public static ShoppingCart createShoppingCartEntity(EntityManager entityManager) { 
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setOrderStatus(DEFAULT_STATUS);
        shoppingCart.setPaymentMethod(DEFAULT_PAYMENT_METHOD);
        shoppingCart.setPlacedDate(DEFAULT_PLACED_DATE);
        shoppingCart.setTotalPrice(DEFAULT_TOTAL_PRICE);
        shoppingCart.setPaymentReference(DEFAULT_PAYMENT_MODIFICATION_REFERENCE);
        shoppingCart.setPaymentModificationReference(DEFAULT_PAYMENT_MODIFICATION_REFERENCE);

        CustomerDetails customerDetails;
        if (TestUtils.findAll(entityManager, ShoppingCart.class).isEmpty()) { 
            customerDetails = CustomerDetailsResourceIT.createCustomerDetails(entityManager);
            entityManager.persist(customerDetails);
            entityManager.flush();
        } else {
            customerDetails = TestUtils.findAll(entityManager, CustomerDetails.class).get(0);
        }

        shoppingCart.setCustomerDetails(customerDetails);
        return shoppingCart;

    }

    public static ShoppingCart createUpdatedEntity(EntityManager em) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setOrderStatus(DEFAULT_STATUS);
        shoppingCart.setPaymentMethod(DEFAULT_PAYMENT_METHOD);
        shoppingCart.setPlacedDate(DEFAULT_PLACED_DATE);
        shoppingCart.setTotalPrice(DEFAULT_TOTAL_PRICE);
        shoppingCart.setPaymentReference(DEFAULT_PAYMENT_MODIFICATION_REFERENCE);
        shoppingCart.setPaymentModificationReference(DEFAULT_PAYMENT_MODIFICATION_REFERENCE);

        // Add required entity
        CustomerDetails customerDetails;
        if (TestUtils.findAll(em, CustomerDetails.class).isEmpty()) {
            customerDetails = CustomerDetailsResourceIT.createCustomerDetails(em);
            em.persist(customerDetails);
            em.flush();
        } else {
            customerDetails = TestUtils.findAll(em, CustomerDetails.class).get(0);
        }
        shoppingCart.setCustomerDetails(customerDetails);
        return shoppingCart;
    }
}
