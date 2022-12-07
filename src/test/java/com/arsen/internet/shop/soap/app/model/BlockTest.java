package com.arsen.internet.shop.soap.app.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BlockTest {

    @Test
    public void testBlock(){

        UserBlock block = new UserBlock();
        block.setUser(new User());

        long current = System.currentTimeMillis() / 1000;
        block.setStartTime(current);
        block.setEndTime(current + (60 * 5));
        block.setReason("Test");

        assertTrue(block.isBanned());
        assertNotNull(block.getUser());
        assertEquals("Test", block.getReason());


        block.setStartTime(current - (60 * 5));
        block.setEndTime(current - 1);

        assertFalse(block.isBanned());

    }

}
