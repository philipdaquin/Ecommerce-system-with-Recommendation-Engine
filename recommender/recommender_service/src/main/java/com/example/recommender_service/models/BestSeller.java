package com.example.recommender_service.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestSeller {
    private Integer userId;
    private List<Integer> productIds = new ArrayList<>();
    private String type;
}
