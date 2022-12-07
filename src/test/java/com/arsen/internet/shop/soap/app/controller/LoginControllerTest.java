package com.arsen.internet.shop.soap.app.controller;

import com.arsen.internet.shop.soap.app.dto.user.UserRegisterDto;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LoginControllerTest {


    private static final String ADDRESS = "http://localhost";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserServiceImpl userService;

    @AfterEach
    public void afterEach(){
        SecurityContextHolder.clearContext();
    }

    @Test
    public void checkAccess() throws Exception {

        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(ADDRESS + "/login"));

    }


    @Test
    public void testLoginIncorrect() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "arsen@mail.com")
                        .param("password", "1111"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    public void testLogin() throws Exception {

        mockMvc.perform(post("/login")
                        .param("username", "arsen@mail.com")
                        .param("password", "2222"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }

    @Test
    public void testIncorrectRegister() throws Exception {

        UserRegisterDto registerDto = new UserRegisterDto();
        registerDto.setFirstName("Andrii");
        registerDto.setLastName("Batkovych");
        registerDto.setEmail("iphone@pnu.edu.ua");
        registerDto.setLogin("Login");
        registerDto.setPassword("weakpass"); //<---
        registerDto.setRepeatPassword("weakpass");
        registerDto.setBirthDate("2022-09-01");

        mockMvc.perform(post("/register").flashAttr("user", registerDto))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(2));

        registerDto.setPassword("AAAABBBBb44");
        registerDto.setRepeatPassword(registerDto.getPassword());

        //Now existing email & login
        registerDto.setLogin("ArsenOOO7");
        registerDto.setEmail("arsen@mail.com");

        mockMvc.perform(post("/register").flashAttr("user", registerDto))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors())
                .andExpect(model().errorCount(2));

    }



    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testCorrectRegistration() throws Exception {

        UserRegisterDto registerDto = new UserRegisterDto();
        registerDto.setFirstName("Andrii");
        registerDto.setLastName("Batkovych");
        registerDto.setEmail("iphone@pnu.edu.ua");
        registerDto.setLogin("Login");
        registerDto.setPassword("AAAABBBBb44");
        registerDto.setRepeatPassword("AAAABBBBb44");
        registerDto.setBirthDate("2022-09-01");

        mockMvc.perform(post("/register").flashAttr("user", registerDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        User user = userService.readById(2);
        assertNotNull(user);

    }

}
