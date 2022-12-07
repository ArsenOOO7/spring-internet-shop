package com.arsen.internet.shop.soap.app.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class UserTest {

    @Test
    public void testUser(){

        User user = new User();
        user.setFirstName("Arsen");
        user.setLastName("Sydoryk");
        user.setEmail("arsen.sydoryk@gmail.com");
        user.setLogin("ArsenOOO7");
        user.setPassword("mySTRONGpassword123");
        user.setBirthDate(Date.valueOf("2004-03-15"));
        user.setBalance(100.0);

        Role testRole = new Role();
        testRole.setName("ADMIN");
        user.setRole(testRole);

        assertEquals("Arsen", user.getFirstName());
        assertEquals("Sydoryk", user.getLastName());
        assertEquals("arsen.sydoryk@gmail.com", user.getEmail());
        assertEquals("ArsenOOO7", user.getLogin());
        assertEquals("mySTRONGpassword123", user.getPassword());
        assertEquals(Date.valueOf("2004-03-15"), user.getBirthDate());
        assertEquals(100.0, user.getBalance());

        assertEquals("ADMIN", user.getRole().getName());

    }

}
