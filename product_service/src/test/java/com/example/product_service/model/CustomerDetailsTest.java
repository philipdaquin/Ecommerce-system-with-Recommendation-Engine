package com.example.product_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;

public class CustomerDetailsTest {
    
    @Test
    public void equalsVerifier() throws Exception { 
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setId(1L);

        CustomerDetails customerDetails2 = new CustomerDetails();
        customerDetails2.setId(customerDetails.getId());

        assertEquals(customerDetails.getId(), customerDetails2.getId());

        customerDetails2.setId(2L);
        assertNotEquals(customerDetails.getId(), customerDetails2.getId());
        
        customerDetails2.setId(null);
        assertNotEquals(customerDetails.getId(), customerDetails2.getId());
    }
}
