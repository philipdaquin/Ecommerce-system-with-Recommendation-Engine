package com.example.product_service.controller.errors;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    public LoginAlreadyUsedException() {
        super(
            ErrorConstants.LOGIN_ALREADY_USED_TYPE, 
            "Login name is used", 
            "accountService", 
            "userExists"
        );
    }
}
