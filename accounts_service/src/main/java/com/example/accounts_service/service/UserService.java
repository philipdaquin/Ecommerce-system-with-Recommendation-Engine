package com.example.accounts_service.service;

import java.util.List;
import java.util.Optional;

import com.example.accounts_service.dto.UserDTO;
import com.example.accounts_service.models.UserInfo;

public interface UserService {
    Optional<UserInfo> findByUsername(String userName);
    Optional<UserInfo> findByEmail(String email);
    Boolean deletedUserById(Long userId);
    UserDTO createUser(UserDTO userDTO);
    List<UserInfo> finalAllUsers(); 
}

