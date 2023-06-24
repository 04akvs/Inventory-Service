package com.inventoryService.service;

import com.inventoryService.entity.Orders;

public interface OrderService {

    double getAmountToBePaid(String customerId);

    Orders obtainBillInfo(String customerId);

    void createOrder(String customerId);

}
