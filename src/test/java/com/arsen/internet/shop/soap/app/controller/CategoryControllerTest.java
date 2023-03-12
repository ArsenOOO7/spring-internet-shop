package com.arsen.internet.shop.soap.app.controller;

import com.arsen.internet.shop.soap.app.model.Category;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.service.CategoryService;
import com.arsen.internet.shop.soap.app.service.RoleService;
import com.arsen.internet.shop.soap.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleService roleService;

    @MockBean
    private CategoryService categoryService;



    @Test
    public void checkWithoutAdmin() throws Exception {
        User user = userService.readById(1);
        mockMvc.perform(get("/category").with(user(user)))
                .andExpect(status().is(404));
    }

    @Test
    public void categoriesGet() throws Exception {

        User user = userService.readById(1);
        user.setRole(roleService.readByName("ADMIN"));

        List<Category> expected = getList(5);
        Page<Category> page = mock(Page.class);

        when(page.getContent()).thenReturn(expected);
        when(page.getTotalPages()).thenReturn(1);

        when(categoryService.getAll(anyInt())).thenReturn(page);

        mockMvc.perform(get("/category").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("category/category"))
                .andExpect(model().attributeExists("categories", "page", "pages"))
                .andExpect(model().attribute("categories", expected))
                .andExpect(model().attribute("page", 1))
                .andExpect(model().attribute("pages", 1));

    }


    @Test
    public void categoriesAddGet() throws Exception {
        User user = userService.readById(1);
        user.setRole(roleService.readByName("ADMIN"));
        mockMvc.perform(get("/category/add").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("category"))
                .andExpect(view().name("category/category_add"));

    }


    @Test
    public void categoriesAddPost() throws Exception {

        User user = userService.readById(1);
        user.setRole(roleService.readByName("ADMIN"));

        Category category = getCategory(1);
        when(categoryService.create(any(Category.class))).thenAnswer(ans -> ans.getArgument(0));

        mockMvc.perform(post("/category/add")
                        .with(user(user))
                        .flashAttr("category", category))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/category"));

    }


    @Test
    public void delete() throws Exception {
        User user = userService.readById(1);
        user.setRole(roleService.readByName("ADMIN"));

        Category category = getCategory(1);
        when(categoryService.readById(anyLong())).thenReturn(category);

        mockMvc.perform(get("/category/delete/1")
                        .with(user(user)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/category"));

    }


    @Test
    public void editGet() throws Exception {
        User user = userService.readById(1);
        user.setRole(roleService.readByName("ADMIN"));

        Category category = getCategory(1);
        when(categoryService.readById(anyLong())).thenReturn(category);

        mockMvc.perform(get("/category/edit/1")
                        .with(user(user)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("category"))
                .andExpect(view().name("category/category_edit"));

    }



    @Test
    public void editPost() throws Exception{
        User user = userService.readById(1);
        user.setRole(roleService.readByName("ADMIN"));

        Category category = getCategory(1);
        when(categoryService.readById(anyLong())).thenReturn(category);
        when(categoryService.update(any(Category.class))).thenAnswer(ans -> ans.getArgument(0));

        mockMvc.perform(post("/category/edit/1")
                        .flashAttr("category", category)
                        .with(user(user)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/category"));

    }

    private static Category getCategory(int i){
        Category category = new Category();
        category.setId(i);
        category.setIdentifier("category " + i);
        category.setLocaleUa("Категорія " + i);
        category.setLocaleEn("Category " + i);

        return category;
    }

    private static List<Category> getList(int n){
        return IntStream.rangeClosed(1, n)
                .mapToObj(CategoryControllerTest::getCategory)
                .toList();
    }

}
