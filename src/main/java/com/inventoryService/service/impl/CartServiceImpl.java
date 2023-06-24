package com.inventoryService.service.impl;

import com.inventoryService.entity.Cart;
import com.inventoryService.entity.Product;
import com.inventoryService.service.CartService;
import com.inventoryService.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductService productService;
    @Autowired
    private RedisTemplate<String, Cart> redisTemplate;

    private final String HASH_KEY  = "Carts";
    public Product addProductInCart(String customerId, Integer productId) {

        Product product = productService.getProductById(productId);
        if(redisTemplate.opsForHash().hasKey(HASH_KEY, customerId))
        {
            Cart cart = (Cart) redisTemplate.opsForHash().get(HASH_KEY,customerId);
            List<Product> productList = cart.getProductList();
            Boolean productFound = false;
            for(int i=0;i<productList.size();i++)
            {
                Product productInList = productList.get(i);
                Integer id = productInList.getProductId();

                if(id.compareTo(productId)==0) {
                    Integer quantity= productInList.getQuantity();
                    productInList.setQuantity(quantity+1);
                    cart.setProductList(productList);
                    productFound = true;
                }
            }
            if(!productFound)
            {
                cart.getProductList().add(product);
            }
            redisTemplate.opsForHash().put(HASH_KEY, customerId, cart);
        }
        else
        {
            List<Product> productList = new ArrayList<>();
            productList.add(product);
            Cart cart = new Cart(customerId, productList);
            redisTemplate.opsForHash().put(HASH_KEY, customerId, cart);
        }
        return product;
    }
    public List<Product> getAllProductsInCart(String customerId)
    {
        Cart cart = (Cart) redisTemplate.opsForHash().get(HASH_KEY,customerId);
        return cart.getProductList();
    }

    public List<Product> deleteProdFromCart(String customerId, Integer productId)
    {
        List<Product> productList = getAllProductsInCart(customerId);
        for(int i=0;i<productList.size();i++)
        {
            Product product = productList.get(i);
            Integer id = product.getProductId();

            if(id.compareTo(productId)==0) {
                productList.remove(i);
                Cart cart = (Cart) redisTemplate.opsForHash().get(HASH_KEY,customerId);
                cart.setProductList(productList);
                redisTemplate.opsForHash().put(HASH_KEY, customerId, cart);
            }
        }
        return productList;
    }

    public void deleteCart(String customerId)
    {
        redisTemplate.opsForHash().delete(HASH_KEY, customerId);
    }
}
