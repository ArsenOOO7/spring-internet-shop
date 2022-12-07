package com.arsen.internet.shop.soap.app.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class CategoryTest {

    @Test
    public void testCategory(){

        Category category = new Category();
        category.setIdentifier("laptop");
        category.setLocaleUa("Ноутбуки");
        category.setLocaleEn("Laptops");

        assertEquals("laptop", category.getIdentifier());
        assertEquals("Ноутбуки", category.getLocaleUa());
        assertEquals("Laptops", category.getLocaleEn());

    }

}
