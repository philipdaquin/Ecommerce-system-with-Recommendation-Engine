package com.example.product_service.controller.errors;



public class EntityNotFoundException extends BadRequestAlertException {

    public EntityNotFoundException(String entityName) {
        super(
            entityName + " not found!", 
            entityName
        );
    }
    
}
