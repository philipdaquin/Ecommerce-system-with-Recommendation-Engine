package com.example.accounts_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.accounts_service.models.UserInfo;

@Repository
public interface UserRespository extends JpaRepository<UserInfo, Long> {    
    Optional<UserInfo> findByEmail(String email);
    Optional<UserInfo> findByUserName(String userName);
}
