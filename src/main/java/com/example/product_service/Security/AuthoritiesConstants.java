package com.example.product_service.Security;

import lombok.NoArgsConstructor;

/**
 * Constants for Spring Security Authorities 
 */
@NoArgsConstructor
public final class AuthoritiesConstants {
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String GUEST = "ROLE_GUEST";
}
