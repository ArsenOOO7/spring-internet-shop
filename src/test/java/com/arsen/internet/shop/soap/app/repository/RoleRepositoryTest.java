package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;


    @Test
    public void createRoleTest(){

        Role role = new Role();
        role.setName("Moderator");

        role = roleRepository.save(role);
        assertEquals(3, role.getId());

    }



    @Test
    public void updateRole(){

        Role role = roleRepository.readRoleByName("ADMIN");
        role.setName("Moderator");

        roleRepository.save(role);
        Role updated = roleRepository.findById(2L).orElse(null);

        assertNotNull(updated);
        assertEquals("Moderator", updated.getName());

    }


    @Test
    public void deleteRoleTest(){
        Role role = new Role();
        role.setName("Moderator");

        role = roleRepository.save(role);
        roleRepository.delete(role);
        assertNull(roleRepository.readRoleByName("Moderator"));
    }

}
