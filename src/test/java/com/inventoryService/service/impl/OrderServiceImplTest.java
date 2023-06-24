package com.inventoryService.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventoryService.entity.Cart;
import com.inventoryService.entity.Orders;
import com.inventoryService.entity.Product;
import com.inventoryService.repository.OrdersRepository;
import com.inventoryService.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private ProductService productService = new ProductServiceImpl();
    @Mock
    private RedisTemplate<String, Cart> redisTemplate;
    @Mock
    private CartServiceImpl cartService;
    @InjectMocks
    private OrderServiceImpl orderService = new OrderServiceImpl();
    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private KafkaProducerService kafkaProducer;
    private List<Product>productList;
    private Cart cart;
    private Orders order;
    @Before
    public void setup()
    {
        productList = new ArrayList<>();
        productList.add(new Product(1, "Nirma", 230.5, "Washing Powder", 2));
        productList.add(new Product(2, "Oranges", 32, "Fruits", 8));

        cart = new Cart("1",productList);
    }
    @Test
    public void getAmountToBePaidTest()
    {
        when(cartService.getAllProductsInCart("1")).thenReturn(productList);
        assertEquals(717.0, orderService.getAmountToBePaid("1"));
    }

    @Test
    public void createOrderTest() throws JsonProcessingException {
        when(cartService.getAllProductsInCart("1")).thenReturn(productList);
        ObjectMapper objectMapper = new ObjectMapper();

        String expectedJSONString = objectMapper.writeValueAsString(productList);
        order = new Orders("1",expectedJSONString,"completed",717.0);
        orderService.createOrder("1");

        verify(cartService).deleteCart("1");
        verify(ordersRepository).save(order);
    }

    @Test
    public void obtainBillInfoTest()
    {
        order = new Orders("1", "ProductList", "Completed", 1200);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        when(ordersRepository.findById("1")).thenReturn(Optional.ofNullable(order));
        orderService.obtainBillInfo("1");
        verify(kafkaProducer).sendMessage(argumentCaptor.capture());
        assertEquals("Order Status Completed", argumentCaptor.getValue());

    }

}
