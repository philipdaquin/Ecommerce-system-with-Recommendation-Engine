package com.example.product_service.service;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.product_service.IntegrationTest;
import com.example.product_service.Security.AuthoritiesConstants;
import com.example.product_service.model.UserInfo;
import com.example.product_service.repository.UserRespository;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
public class UserResourceIT {
    
    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String UPDATED_LOGIN = "jhipster";

    private static final Long DEFAULT_ID = 1L;

    private static final String DEFAULT_PASSWORD = "passjohndoe";
    private static final String UPDATED_PASSWORD = "passjhipster";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "jhipster@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    private static final String DEFAULT_LASTNAME = "doe";
    private static final String UPDATED_LASTNAME = "jhipsterLastName";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

    private static final String DEFAULT_LANGKEY = "en";
    private static final String UPDATED_LANGKEY = "fr";

    @Autowired
    private UserRespository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private MockMvc restUserMockMvc;

    private UserInfo user;

    public static UserInfo createEntity(EntityManager em) {
        UserInfo user = new UserInfo();
        user.setUserLogin(DEFAULT_LOGIN);
        user.setPassword(DEFAULT_PASSWORD);
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLanguage(DEFAULT_LANGKEY);
        return user;
    }

    /**
     * Setups the database with one user.
     */
    public static UserInfo initTestUser(UserRespository userRepository, EntityManager em) {
        UserInfo user = createEntity(em);
        user.setUserLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        return user;
    }
}
