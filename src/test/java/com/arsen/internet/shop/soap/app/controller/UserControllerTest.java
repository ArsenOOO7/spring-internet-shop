package com.arsen.internet.shop.soap.app.controller;

import com.arsen.internet.shop.soap.app.dto.user.UserEditDto;
import com.arsen.internet.shop.soap.app.dto.user.UserPasswordChangeDto;
import com.arsen.internet.shop.soap.app.dto.user.UserTopUpDto;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    public void profileCheck() throws Exception {

        User user = userService.readById(1);
        String body = mockMvc.perform(get("/profile").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(body.contains("Arsen Sydoryk"));
        assertTrue(body.contains("arsen@mail.com"));
    }


    @Test
    public void topUpGet() throws Exception {
        User user = userService.readById(1);

        mockMvc.perform(get("/topup").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("topup"))
                .andExpect(model().attributeExists("topUp"));

    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void topUpPost() throws Exception{

        User user = userService.readById(1);
        double oldMoney = user.getBalance();

        UserTopUpDto topUpDto = new UserTopUpDto();
        topUpDto.setMoney(1000);

        mockMvc.perform(post("/topup").with(user(user))
                        .flashAttr("topUp", topUpDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));


        user = userService.readById(1);
        assertEquals(oldMoney + topUpDto.getMoney(), user.getBalance());

    }

    @Test
    public void editGet() throws Exception{
        User user = userService.readById(1);
        mockMvc.perform(get("/edit").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void editPost() throws Exception{

        User user = userService.readById(1);

        UserEditDto userEditDto = new UserEditDto(user);
        userEditDto.setFirstName("Stepan");
        userEditDto.setLastName("Hiha");

        mockMvc.perform(post("/edit").with(user(user))
                        .flashAttr("user", userEditDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        user = userService.readById(1);
        assertEquals("Stepan", user.getFirstName());
        assertEquals("Hiha", user.getLastName());

    }


    @Test
    public void editPasswordGet() throws Exception {
        User user = userService.readById(1);
        mockMvc.perform(get("/edit/password").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("password_change"))
                .andExpect(model().attributeExists("user"));
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void editPasswordPost() throws Exception {

        User user = userService.readById(1);
        UserPasswordChangeDto passwordChangeDto = new UserPasswordChangeDto();
        passwordChangeDto.setOldPassword("2222");
        passwordChangeDto.setNewPassword("AAAABBBBb443");
        passwordChangeDto.setRepeatPassword("AAAABBBBb443");

        mockMvc.perform(post("/edit/password").with(user(user)).flashAttr("user", passwordChangeDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

    }

}
