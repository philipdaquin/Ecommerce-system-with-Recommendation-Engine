package com.example.product_service.controller.errors;

import java.net.URI;

public class InvalidPasswordException extends BadRequestAlertException {

    public InvalidPasswordException(String entityName, String errorKey) {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Invalis Password!", entityName, errorKey);
    }
    
}
