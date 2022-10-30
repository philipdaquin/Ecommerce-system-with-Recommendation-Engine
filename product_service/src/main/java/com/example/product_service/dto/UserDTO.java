package com.example.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO { 
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
}