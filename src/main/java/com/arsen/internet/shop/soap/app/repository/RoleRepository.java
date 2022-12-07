package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Role repository
 * @author Arsen Sydoryk
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {


    /**
     * Look for role by name
     * @param name of role
     * @return Role
     */
    Role readRoleByName(String name);

}
