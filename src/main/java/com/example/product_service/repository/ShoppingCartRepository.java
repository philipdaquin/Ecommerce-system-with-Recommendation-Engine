package com.example.product_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.product_service.model.ShoppingCart;
import com.example.product_service.model.enumeration.OrderStatus;

@SuppressWarnings("unused")
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    
    Optional<ShoppingCart> findFirstByCustomerDetailsUserInfoAndStatusOrderByIdAsc(String login, OrderStatus orderStatus);
    
    List<ShoppingCart> findAllByCustomerDetailsUserInfoAndStatusNot(String user, OrderStatus orderStatus);
    
    Optional<ShoppingCart> findOneByPaymentModificationReference(String paymentref);

    Optional<ShoppingCart> findOneByPaymentReference(String paymentRef);
}
