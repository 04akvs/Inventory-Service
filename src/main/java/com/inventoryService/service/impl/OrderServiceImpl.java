package com.inventoryService.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryService.entity.Orders;
import com.inventoryService.entity.Product;
import com.inventoryService.repository.OrdersRepository;
import com.inventoryService.service.CartService;
import com.inventoryService.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private KafkaProducerService kafkaProducer;


    public OrderServiceImpl(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    ObjectMapper objectMapper = new ObjectMapper();
    public double getAmountToBePaid(String customerId)
    {
        List<Product> productList = cartService.getAllProductsInCart(customerId);
        double amount = 0;
        for(int i=0;i<productList.size();i++)
        {
            Product product = productList.get(i);
            amount += product.getQuantity()* (product.getPrice());
        }
        return amount;
    }

    public void createOrder(String customerId)
    {
        List<Product> productList = cartService.getAllProductsInCart(customerId);
        String productListToJson = null;
        try {
            productListToJson = objectMapper.writeValueAsString(productList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Orders order = new Orders(customerId, productListToJson, "completed", getAmountToBePaid(customerId));
        cartService.deleteCart(customerId);
        ordersRepository.save(order);
    }

    public Orders obtainBillInfo(String customerId) {

        kafkaProducer.sendMessage("Order Status " + ordersRepository.findById(customerId).get().getOrderStatus());
        return ordersRepository.findById(customerId).get();
    }
}
