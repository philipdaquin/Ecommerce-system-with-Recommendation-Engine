package com.example.product_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingCartTest {
    
    @Test
    public void equalsVerifier() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.setId(shoppingCart.getId());


        assertThat(shoppingCart).isEqualTo(shoppingCart2);
        assertEquals(shoppingCart.getId(), shoppingCart2.getId());
        
        shoppingCart2.setId(2L);
        assertNotEquals(shoppingCart.getId(), shoppingCart2.getId());

        shoppingCart.setId(null);
        assertNotEquals(shoppingCart.getId(), shoppingCart2.getId());
    }
}
