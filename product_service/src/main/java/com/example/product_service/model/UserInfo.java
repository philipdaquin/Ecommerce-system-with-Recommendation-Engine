package com.example.product_service.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ManyToAny;

import com.example.product_service.config.Constant;
import com.example.product_service.model.enumeration.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Serializable - When you want to be able to convert an intance of a class into a series of bytes
 * Data - a shortcut for ToString, Getter, Setter, RequiredArgsConstructor 
 * Entityy -  An entity representes a class stored in a database, every instance of an entity 
 *  represents a row in the table
 */

@Data 
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userinfo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @NotBlank(message = "userName cannot be empty")
    @NotNull
    @Pattern(regexp = Constant.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(name = "username", length = 50)
    private String userLogin;
    
    @NotBlank(message = "User must have valid first name")
    @Size(max = 50)
    @Column(name = "firstname", length = 50)
    private String firstName;
    
    @Size(max = 50)
    @Column(name = "lastname", length = 50)
    private String lastName;
    
    @NotEmpty(message = "Email is required")
    @NotNull
    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true, nullable = false)
    private String email;
    
    @JsonIgnore
    @NotNull
    @NotBlank(message = "Password is required")
    @Size(min = 10, max = 50)
    @Column(name = "password_hash", length = 50, nullable = false)
    private String password;

    @Column(name = "createdAt")
    private Instant createdAt = null;

    @NotNull
    @Column(nullable = false)
    private boolean enabled = false;

    @Size(min = 2, max = 10)
    @Column(name = "language", length = 10)
    private String language;


    @Column(name ="imageurl")
    private String imageUrl;

    @Size(max = 20)
    @JsonIgnore
    @Column(name ="activation_key")
    private String activationKey;

    @Size(max = 20)
    @JsonIgnore
    @Column(name ="image_url")
    private String resetKey;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

}
