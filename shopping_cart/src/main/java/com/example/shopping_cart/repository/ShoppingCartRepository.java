package com.example.shopping_cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shopping_cart.models.ShoppingCart;
import com.example.shopping_cart.models.enumeration.OrderStatus;

@SuppressWarnings("unused")
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    
    Optional<ShoppingCart> findFirstByCustomerDetailsUserLoginAndStatusOrderByIdAsc(String login, OrderStatus orderStatus);
    
    List<ShoppingCart> findAllByCustomerDetailsUserLoginAndStatusNot(String user, OrderStatus orderStatus);
    
    Optional<ShoppingCart> findOneByPaymentModificationReference(String paymentref);

    Optional<ShoppingCart> findOneByPaymentReference(String paymentRef);
}
