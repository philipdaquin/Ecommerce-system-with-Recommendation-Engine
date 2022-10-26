package com.example.product_service.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.example.product_service.model.enumeration.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull
    @Column(name = "name", length = 254)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "brandname", nullable = true)
    private String brandName;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    @DecimalMin(value = "0")
    private BigDecimal pricePerUnit;

    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] image;

    @Column(name = "image_content_type")
    private String contentType;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_size", nullable = false)
    private Size itemSize;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @NotBlank(message = "Product Category must be set in the product")
    @JsonIgnoreProperties(value = {"products"}, allowGetters = true)
    private ProductCategory productCategory;


}
