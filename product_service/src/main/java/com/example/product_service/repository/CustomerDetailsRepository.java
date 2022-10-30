package com.example.product_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.product_service.model.CustomerDetails;



@SuppressWarnings("unused")
@Repository
public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Long> {

    Optional<CustomerDetails> findOneByUserLogin(String user);
    
}
