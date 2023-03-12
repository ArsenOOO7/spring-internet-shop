package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.model.UserBlock;
import com.arsen.internet.shop.soap.app.repository.UserBlockRepository;
import com.arsen.internet.shop.soap.app.repository.UserRepository;
import com.arsen.internet.shop.soap.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserBlockRepository blockRepository;

    @Autowired
    private UserServiceImpl userService;

    private User user;


    @BeforeEach
    public void setUp(){

        user = new User();
        user.setId(1);
        user.setFirstName("Nick");
        user.setLastName("Green");
        user.setEmail("nick@mail.com");
        user.setPassword("2222");

    }


    @Test
    public void createEmptyUser(){
        assertThrows(NullEntityReferenceException.class, () -> userService.create(null));
    }


    @Test
    public void checkMockWorking(){
        when(userRepository.save(any(User.class))).thenReturn(null);
        assertNull(userService.create(user));
    }

    @Test
    public void createNormalUser(){

        when(userRepository.save(any(User.class))).thenAnswer(answer -> answer.getArgument(0));

        User created = userService.create(user);

        assertNotNull(created);
        assertEquals("Nick", user.getFirstName());

        assertNotNull(user.getRole());
        assertEquals("user", user.getRole().getName().toLowerCase());

        verify(userRepository).save(any(User.class));

    }


    @Test
    public void checkReadingById(){

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertEquals(user, userService.readById(1L));

    }


    @Test
    public void checkReadingByIncorrectId(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.readById(1));
    }


    @Test
    public void checkReadingByEmail(){
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        assertEquals(user, userService.readByEmail(user.getEmail()));
    }

    @Test
    public void checkReadingByIncorrectEmail(){
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> userService.readByEmail(user.getEmail()));
    }

    @Test
    public void checkUpdateNullUser(){
        assertThrows(NullEntityReferenceException.class, () -> userService.update(null));
    }


    @Test
    public void checkUpdateEmptyUser(){

        when(userRepository.save(any(User.class))).thenAnswer(answer -> answer.getArgument(0));
        User user = new User();
        user.setId(1);

        assertThrows(EntityNotFoundException.class, () -> userService.update(user));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertEquals(user, userService.update(user));

    }


    @Test
    public void checkUpdateNormalUser(){
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(answer -> answer.getArgument(0));

        assertEquals(user, userService.update(user));
    }


    @Test
    public void checkDeleteNullUser(){
        assertThrows(EntityNotFoundException.class, () -> userService.delete(0));
    }


    @Test
    public void checkDeleteNormalUser(){

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.delete(user.getId()));

    }


    @Test
    public void getAll(){
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> users = userService.getAll();
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    public void getAllPageTest(){

        Page<User> page = mock(Page.class);

        when(page.getTotalPages()).thenReturn(1);
        when(page.getContent()).thenReturn(List.of(user));

        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<User> result = userService.getAll(1);

        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getContent().size());
        assertEquals(user, result.getContent().get(0));

    }


    @Test
    public void getAllBanned(){


        Page<UserBlock> page = mock(Page.class);

        when(page.getTotalPages()).thenReturn(0);
        when(page.getContent()).thenReturn(List.of());

        when(blockRepository.findAllBy(any(Pageable.class))).thenReturn(page);

        Page<UserBlock> result = userService.getAllBanned(1);
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getContent().size());


    }

}
