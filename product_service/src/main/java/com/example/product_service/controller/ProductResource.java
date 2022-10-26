package com.example.product_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = {"/api"}, method = {
    RequestMethod.GET, 
    RequestMethod.DELETE, 
    RequestMethod.POST
})
@AllArgsConstructor
public class ProductResource {
    
    private static final String ENTITY = "product";

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private final ProductService productService;
    
    private final ProductRepository productRepository;

    @Value("${application_name}")
    private final String applicationName;
}
