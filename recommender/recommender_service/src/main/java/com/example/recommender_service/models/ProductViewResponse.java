package com.example.recommender_service.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductViewResponse {
    
    private Integer userId;

    private List<Integer> productIds = new ArrayList<>();

    private String type;
}
