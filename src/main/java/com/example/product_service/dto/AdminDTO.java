package com.example.product_service.dto;

import java.time.Instant;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.product_service.config.Constant;
import com.example.product_service.model.enumeration.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  A DTO representing a USER, with his authorities 
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminDTO { 
    
    private Long id;

    /*
     * Non-nullable and needs to have 1 whitespace 
     */
    @NotBlank
    @Pattern(regexp = Constant.LOGIN_REGEX)
    @Size(min = 1, max = 50)
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

    private Gender gender;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String country;

    private String lastModifiedBy;
    
    private Instant lastModifiedDate;

    private Set<String> authorities; 
}