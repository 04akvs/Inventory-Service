package com.inventoryService.service.impl;

import com.inventoryService.entity.Cart;
import com.inventoryService.entity.Product;
import com.inventoryService.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;
    @Mock
    private ProductService productService;

    @Mock
    private RedisTemplate<String, Cart> redisTemplate;

    @Mock
    private HashOperations hashOperations;

    private final String HASH_KEY  = "Carts";
    private List<Product> productList;
    private Product product;
    @Mock
    private Cart cart;

    @Before
    public void setup()
    {
        product = new Product(1,"Nivea",234,"Body Lotion",5);
        productList = new ArrayList<>();
        productList.add(product);
        cart = new Cart("1", productList);
    }
    @Test
    public void addProductInCartWhenCartIsEmptyTest()
    {
        when(productService.getProductById(1)).thenReturn(product);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.hasKey(HASH_KEY,  "1")).thenReturn(false);
        assertEquals(product, cartService.addProductInCart("1",1));
    }

    @Test
    public void addProductInCartWhenCartIsNotEmptyTest()
    {
        when(productService.getProductById(1)).thenReturn(product);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.hasKey(HASH_KEY,  "1")).thenReturn(true);

        when(hashOperations.get(HASH_KEY,"1")).thenReturn(cart);
        assertEquals(product, cartService.addProductInCart("1",1));
    }
    @Test
    public void getAllProductsInCartTest()
    {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(HASH_KEY,"1")).thenReturn(cart);
        assertEquals(productList, cartService.getAllProductsInCart("1"));
    }

    @Test
    public void deleteProductInCartTest()
    {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(HASH_KEY,"1")).thenReturn(cart);
        assertEquals(productList, cartService.deleteProdFromCart("1",1));
    }

    @Test
    public void deleteCartTest()
    {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        cartService.deleteCart("1");
    }
}
