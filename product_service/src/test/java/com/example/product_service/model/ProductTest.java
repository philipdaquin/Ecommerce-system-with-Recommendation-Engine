package com.example.product_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;

public class ProductTest {
    
    @Test
    public void equalsVerifier() throws Exception { 
        Product product = new Product();
        product.setId(1L);

        Product product2 = new Product();
        product2.setId(product.getId());

        assertEquals(product.getId(), product2.getId());

        product2.setId(2L);
        assertNotEquals(product.getId(), product2.getId());
        
        product2.setId(null);
        assertNotEquals(product.getId(), product2.getId());
    }
}
