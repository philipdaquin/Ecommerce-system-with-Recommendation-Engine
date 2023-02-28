package com.example.product_service.dto;

import java.time.Instant;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Public Attributes reepresenting the user
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO { 
    
    private Long id;

    private String userLogin;
    
    @Size(max = 40)
    private String firstName;
    
    @Size(max = 40)
    private String lastName;
    
    @Email
    @Size(min = 5, max = 254)
    private String email;

    private String password;

    private Instant createdAt;

    private boolean enabled;

    @Size(min = 2, max = 10)
    private String language;

    @Size(max = 256)
    private String imageUrl;

    private String activationKey;

    private String resetKey;

    private boolean activated = false;

}