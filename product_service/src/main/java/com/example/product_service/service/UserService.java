package com.example.product_service.service;

import java.util.List;
import java.util.Optional;

import com.example.product_service.dto.UserDTO;
import com.example.product_service.model.UserInfo;

public interface UserService {
    Optional<com.example.product_service.model.UserInfo> findByUsername(String userName);
    Optional<UserInfo> findByEmail(String email);
    Boolean deletedUserById(Long userId);
    UserDTO createUser(UserDTO userDTO);
    List<UserInfo> finalAllUsers(); 
}

