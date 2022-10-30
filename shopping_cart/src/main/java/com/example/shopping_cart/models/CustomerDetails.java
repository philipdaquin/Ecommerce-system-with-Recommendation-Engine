package com.example.shopping_cart.models;

import java.util.HashSet;
import java.util.Set;

import com.example.shopping_cart.models.enumeration.Gender;

public class CustomerDetails {
    private Gender gender;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
    private User user;
    private Set<ShoppingCart> carts = new HashSet<>();
}
