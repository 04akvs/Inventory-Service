package com.inventoryService.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "customer")
public class Customer {

    @Id
    @Column(name = "customerId", unique = true, nullable = false)
    private String customerId;

    @Column(name = "customerName", nullable = false)
    private String customerName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @JsonProperty
    @Column(name = "isAdmin")
    private boolean admin;

}
