package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void createCategoryTest(){

        Category category = new Category();
        category.setIdentifier("test");
        category.setLocaleUa("Тести");
        category.setLocaleEn("Tests");

        category = categoryRepository.save(category);
        System.out.println(categoryRepository.findAll());
        assertEquals(2, category.getId());

    }


    @Test
    public void updateCategoryTest(){

        Category category = categoryRepository.findById(1L).orElse(null);

        assertNotNull(category);
        assertEquals(1, category.getId());

        category.setIdentifier("laptops");
        categoryRepository.save(category);

        Category updated = categoryRepository.findById(1L).orElse(null);

        assertNotNull(updated);
        assertEquals("laptops", category.getIdentifier());

    }


    @Test
    public void deleteCategoryTest(){

        categoryRepository.deleteById(1L);
        Category category = categoryRepository.findById(1L).orElse(null);

        assertNull(category);
    }

}
