package com.example.accounts_service.models;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @NotBlank(message = "userName cannot be empty")
    private String userName;
    
    @NotBlank(message = "User must have valid first name")
    private String firstName;
    
    @NotBlank(message = "User must have valid last name")
    private String lastName;
    
    @Email(message = "")
    @NotEmpty(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    private Instant createdAt;
    private boolean enabled;
}
