package com.example.product_service.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {
    private Long Id;
    private String name;
    private String description;
    private String brandName;
    private BigDecimal pricePerUnit;
    private Long noOfStocksl;
    private String imageUrl;
}
