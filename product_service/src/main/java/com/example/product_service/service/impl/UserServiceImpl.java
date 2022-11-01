package com.example.product_service.service.impl;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.product_service.dto.UserDTO;
import com.example.product_service.model.UserInfo;
import com.example.product_service.repository.UserRespository;
import com.example.product_service.service.UserService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRespository userRespository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        var new_user = new UserInfo();
        new_user.setFirstName(userDTO.getFirstName());
        new_user.setLastName(userDTO.getLastName());
        new_user.setEmail(userDTO.getEmail());
        
        String password = passwordEncoder.encode(userDTO.getPassword());
        new_user.setPassword(password);
        new_user.setEnabled(false);
        new_user.setCreatedAt(Instant.now());

        userRespository.save(new_user);

        return userDTO;
    }

    @Override
    public Boolean deletedUserById(Long userId) {
        UserInfo user = userRespository
            .findById(userId)
            .orElseThrow(
                () -> new IllegalStateException("Unable to get the user in the database"));
        userRespository.delete(user);
        return userRespository.findById(userId).isPresent(); 
    }

    @Override
    public Optional<UserInfo> findByEmail(String email) {
        return userRespository.findByEmail(email);
    }

    @Override
    public Optional<UserInfo> findByUsername(String userName) {
        return userRespository.findByUserName(userName);
    }

    @Override
    public List<UserInfo> finalAllUsers() {
        return userRespository.findAll();
    }
    
    
}
