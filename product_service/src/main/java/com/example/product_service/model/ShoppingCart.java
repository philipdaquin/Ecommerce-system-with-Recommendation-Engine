package com.example.product_service.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.example.product_service.model.enumeration.OrderStatus;
import com.example.product_service.model.enumeration.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "shopping_cart")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShoppingCart implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    OrderStatus orderStatus;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    PaymentMethod paymentMethod;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @NotNull
    @Column(name = "placeddate", nullable = false)
    Instant placedDate;

    @Column(name = "payment_reference")
    String paymentReference;

    @Column(name = "payment_modification_reference")
    String paymentModificationReference;
    
    @OneToMany(mappedBy = "cart")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = "cart", allowSetters = true)
    Set<ProductOrder> productOrder = new HashSet<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnoreProperties(value = {"user", "carts"}, allowSetters = true)
    private CustomerDetails customerDetails;

    public void calculateTotalPrice() { 
        if (this.productOrder != null) { 
            this.setTotalPrice(this.productOrder
                .stream()
                .map(ProductOrder::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        }
    }
    public ShoppingCart removeOrder(ProductOrder productOrder) { 
        this.productOrder.remove(productOrder);
        productOrder.setShoppingCart(null);
        calculateTotalPrice();
        return this;
    }

    public ShoppingCart orders(Set<ProductOrder> productOrders) { 
        this.setProductOrder(productOrders);
        calculateTotalPrice();
        return this;
    }
    public ShoppingCart addCart(ProductOrder productOrder) { 
        // Add productOrder to current ProductOrder
        this.productOrder.add(productOrder);
        // Update the shopping cart in the productOrder
        productOrder.setShoppingCart(this);
        // Recalculate 
        calculateTotalPrice();
        return this;
    }
}
