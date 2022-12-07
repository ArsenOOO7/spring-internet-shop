package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Category;
import com.arsen.internet.shop.soap.app.repository.CategoryRepository;
import com.arsen.internet.shop.soap.app.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryServiceImpl categoryService;


    private static Category category;
    @BeforeAll
    public static  void tearUp(){
        category = new Category();
        category.setLocaleUa("Ноутбуки");
        category.setLocaleEn("Laptops");
        category.setIdentifier("laptop");
        category.setId(1);
    }


    @Test
    public void createNullCategory(){
        assertThrows(NullEntityReferenceException.class, () -> categoryService.create(null));
    }


    @Test
    public void createNormalCategory(){

        when(categoryRepository.save(any(Category.class))).thenAnswer(ans -> ans.getArgument(0));
        assertEquals(category, categoryService.create(category));

    }


    @Test
    public void updateNullCategory(){
        assertThrows(NullEntityReferenceException.class, () -> categoryService.update(null));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.update(category));
    }


    @Test
    public void updateNormalCategory(){

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(ans -> ans.getArgument(0));

        Category result = categoryService.update(category);
        assertEquals(category, result);

    }



    @Test
    public void testReading(){

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.readById(1));

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        assertEquals(category, categoryService.readById(1));

    }


    @Test
    public void testDelete(){

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.delete(1));

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        assertDoesNotThrow(() -> categoryService.delete(1));

    }


    @Test
    public void testGetAll(){

        List<Category> expected = List.of(category, category);

        when(categoryRepository.findAll()).thenReturn(List.of());
        assertEquals(List.of(), categoryService.getAll());

        when(categoryRepository.findAll()).thenReturn(List.of(category, category));
        assertEquals(expected, categoryService.getAll());

    }


    @Test
    public void testPageGetAll(){

        List<Category> expected = List.of(category, category);
        Page<Category> page = mock(Page.class);

        when(page.getTotalPages()).thenReturn(1);
        when(page.getContent()).thenReturn(List.of(category, category));

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<Category> result = categoryService.getAll(1);

        assertEquals(1, result.getTotalPages());
        assertEquals(expected, result.getContent());

    }

}
