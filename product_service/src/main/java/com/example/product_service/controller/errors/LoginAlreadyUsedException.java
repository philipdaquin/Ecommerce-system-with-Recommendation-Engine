package com.example.product_service.controller.errors;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    public LoginAlreadyUsedException(String entityName, String errorKey) {
        super(entityName, errorKey);
        //TODO Auto-generated constructor stub
    }
    
}
