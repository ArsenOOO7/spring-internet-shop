package com.arsen.internet.shop.soap.app.model;

import com.arsen.internet.shop.soap.app.model.data.CartStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class CartTest {

    @Test
    public void testCart(){
        Cart cart = new Cart();
        cart.setUser(new User());
        cart.setProduct(new Product());
        cart.setActualPrice(1000);

        Date date = Date.from(Instant.now());
        cart.setDate(date);
        cart.setQuantity(100);
        cart.setStatus(CartStatus.BOUGHT);


        assertNotNull(cart.getUser());
        assertNotNull(cart.getProduct());

        assertEquals(1000, cart.getActualPrice());
        assertEquals(date, cart.getDate());
        assertEquals(100, cart.getQuantity());
        assertEquals(CartStatus.BOUGHT, cart.getStatus());

    }

}
