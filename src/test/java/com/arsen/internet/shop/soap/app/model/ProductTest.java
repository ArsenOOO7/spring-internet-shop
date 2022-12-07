package com.arsen.internet.shop.soap.app.model;

import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.SizeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class ProductTest {

    @Test
    public void testProduct(){

        Product product = new Product();

        product.setShortName("Product");
        product.setFullName("Product full name");
        product.setDescription("Some description");
        product.setQuantity(100);
        product.setPrice(100.0);
        product.setColor(Color.BLACK);
        product.setSizeUnit(SizeUnit.MM);
        product.setSizeValue(1000);

        Category category = new Category();
        category.setId(1);
        category.setIdentifier("test");
        category.setLocaleUa("Тест");
        category.setLocaleEn("Test");

        product.setCategory(category);
        product.setImage(new Image());

        Date date = Date.from(Instant.now());
        product.setCreatedAt(date);

        assertEquals("Product", product.getShortName());
        assertEquals("Product full name", product.getFullName());
        assertEquals("Some description", product.getDescription());
        assertEquals(100, product.getQuantity());
        assertEquals(100.0, product.getPrice());
        assertEquals(Color.BLACK, product.getColor());
        assertEquals(SizeUnit.MM, product.getSizeUnit());
        assertEquals(1000, product.getSizeValue());

        assertNotNull(product.getCategory());
        assertEquals("test", product.getCategory().getIdentifier());

        assertNotNull(product.getImage());
        assertEquals(date, product.getCreatedAt());

    }

}
