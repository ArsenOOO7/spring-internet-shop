package com.arsen.internet.shop.soap.app.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class RoleTest {

    @Test
    public void testRole(){

        Role role = new Role();
        role.setName("ADMIN");

        assertEquals("ADMIN", role.getName());

    }

}
