package com.example.product_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;

public class ProductCategoryTest {

    @Test
    public void equalsVerifier() throws Exception { 
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(1L);

        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setId(productCategory.getId());

        assertEquals(productCategory.getId(), productCategory2.getId());

        productCategory2.setId(2L);
        assertNotEquals(productCategory.getId(), productCategory2.getId());
        
        productCategory2.setId(null);
        assertNotEquals(productCategory.getId(), productCategory2.getId());
    }
}
