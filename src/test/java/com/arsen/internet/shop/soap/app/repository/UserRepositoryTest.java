package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.Role;
import com.arsen.internet.shop.soap.app.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository repository;

    @Test
    public void createUser(){

        Role role = repository.readRoleByName("ADMIN");
        User user = new User();
        user.setFirstName("Arsen");
        user.setLastName("Second");
        user.setEmail("arsen.sydoryk@gmail.com");
        user.setLogin("ArsenOOO8");
        user.setPassword("mySTRONGpassword123");
        user.setBirthDate(Date.valueOf("2004-03-15"));
        user.setBalance(100.0);

        user.setRole(role);
        user = userRepository.save(user);

        System.out.println(userRepository.findAll());
        assertEquals(2, user.getId());

    }

    @Test
    public void checkReadingExistingUserById(){

        User user = userRepository.findById(1L)
                .orElse(null);

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("Arsen", user.getFirstName());

    }

    @Test
    public void checkReadingExistingUserByEmail(){

        User user = userRepository.findByEmail("arsen@mail.com");

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("Arsen", user.getFirstName());

    }

    @Test
    public void checkReadingExistingUserByLogin(){

        User user = userRepository.findByLogin("ArsenOOO7");

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("Arsen", user.getFirstName());

    }


    @Test
    public void checkExistingByEmailAndLogin(){

        assertTrue(userRepository.existsByEmail("arsen@mail.com"));
        assertTrue(userRepository.existsByLogin("ArsenOOO7"));

    }

    @Test
    public void deleteUser(){

        userRepository.deleteById(1L);
        User user = userRepository.findById(1L).orElse(null);

        assertNull(user);

    }


    @Test
    public void checkGettingAll(){

        List<User> users = userRepository.findAll();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());

    }

}
