package com.example.recommender_service.models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productViewHistory")
public class ProductViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Integer productId;

    @Column(name = "event")
    private String event;

    @Column(name = "messageId")
    private String messageId;

    @Column(name = "source")
    private String source;

    @Column(name = "clickTimestamp")
    private Timestamp clickTimestamp;

}
