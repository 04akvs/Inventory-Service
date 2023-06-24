package com.inventoryService.repository;

import com.inventoryService.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, String>{
}
