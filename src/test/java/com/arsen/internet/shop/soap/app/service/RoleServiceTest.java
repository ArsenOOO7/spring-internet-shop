package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Role;
import com.arsen.internet.shop.soap.app.repository.RoleRepository;
import com.arsen.internet.shop.soap.app.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class RoleServiceTest {

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private RoleServiceImpl roleService;

    private static Role role;


    @BeforeAll
    public static void beforeAll() {

        role = new Role();
        role.setId(1);
        role.setName("MODERATOR");

    }


    @Test
    public void createNullRole(){
        assertThrows(NullEntityReferenceException.class, () -> roleService.create(null));
    }


    @Test
    public void createNormalRole(){

        when(roleRepository.save(any(Role.class))).thenAnswer(ans -> ans.getArgument(0));
        assertEquals(role, roleService.create(role));

    }


    @Test
    public void updateNullRole(){
        assertThrows(NullEntityReferenceException.class, () -> roleService.update(null));
    }

    @Test
    public void updateEmptyRole(){

        Role role = new Role();
        role.setName("Guest");
        assertThrows(EntityNotFoundException.class, () -> roleService.update(role));

        when(roleRepository.save(any(Role.class))).thenAnswer(ans -> ans.getArgument(0));
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        assertEquals(role, roleService.update(role));

    }


    @Test
    public void updateNormalRole(){
        when(roleRepository.save(any(Role.class))).thenAnswer(ans -> ans.getArgument(0));
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        assertEquals(role, roleService.update(role));
    }


    @Test
    public void checkReadingById(){

        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        assertEquals(role, roleService.readById(role.getId()));

    }


    @Test
    public void checkReadingByIncorrectId(){

        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> roleService.readById(12));

    }


    @Test
    public void checkReadingByName(){
        when(roleRepository.readRoleByName("MODERATOR")).thenReturn(role);
        assertEquals(role, roleService.readByName("MODERATOR"));
    }


    @Test
    public void checkReadingByWrongName(){
        when(roleRepository.readRoleByName("GUEST")).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> roleService.readByName("GUEST"));
    }


    @Test
    public void checkDelete(){
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        assertDoesNotThrow(() -> roleService.delete(role.getId()));
    }

    @Test
    public void checkNullDelete(){
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> roleService.delete(role.getId()));
    }


    @Test
    public void getAllTest(){
        when(roleRepository.findAll()).thenReturn(List.of(role));
        assertEquals(List.of(role), roleService.getAll());
    }

}
