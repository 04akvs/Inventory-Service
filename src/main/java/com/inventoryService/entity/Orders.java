package com.inventoryService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "Orders")
public class Orders {

    @Id
    @Column (name = "customerId", unique = true, nullable = false)
    private String customerId;

    @Column (name = "listOfProducts")
    private String listOfProducts;

    @Column (name = "orderStatus")
    private String orderStatus;

    @Column (name = "amount")
    private double amount;
}
