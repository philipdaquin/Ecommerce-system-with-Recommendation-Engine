package com.example.product_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ProductOrderTest {
    
    @Test
    public void equalsVerifier() throws Exception { 
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(1L);

        ProductOrder productOrder2 = new ProductOrder();
        productOrder2.setId(productOrder.getId());


        assertThat(productOrder).isEqualTo(productOrder2);
        assertEquals(productOrder.getId(), productOrder2.getId());
        
        productOrder2.setId(2L);
        assertNotEquals(productOrder.getId(), productOrder2.getId());

        productOrder.setId(null);
        assertNotEquals(productOrder.getId(), productOrder2.getId());
    }
}
