package com.example.product_service.service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.product_service.IntegrationTest;
import com.example.product_service.model.CustomerDetails;
import com.example.product_service.model.UserInfo;
import com.example.product_service.model.enumeration.Gender;
import com.example.product_service.repository.CustomerDetailsRepository;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username="admin", authorities={"ROLE_ADMIN"}, password = "admin")
public class CustomerDetailsResourceIT {
    
    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;

    @Autowired
    private UserResourceIT userResourceIT;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc restMockMvc;

    private CustomerDetails customerDetails;


    public static CustomerDetails createCustomerDetails(EntityManager entityManager) {
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setGender(DEFAULT_GENDER);
        customerDetails.setPhone(DEFAULT_PHONE);
        customerDetails.setAddressLine1(DEFAULT_ADDRESS_LINE_1);
        customerDetails.setAddressLine2(DEFAULT_ADDRESS_LINE_2);
        customerDetails.setCity(DEFAULT_CITY);
        customerDetails.setCountry(DEFAULT_COUNTRY);

        UserInfo user = UserResourceIT.createEntity(entityManager);
        entityManager.persist(user);
        entityManager.flush();
        customerDetails.setUserInfo(user);


        return customerDetails;
    }
}
