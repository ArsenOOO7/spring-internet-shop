package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Role;

import javax.persistence.EntityNotFoundException;
import java.util.List;


/**
 * Role Service
 * @author Arsen Sydoryk
 */
public interface RoleService {

    /**
     * Create role
     * @param role to create
     * @return created role
     * @throws NullEntityReferenceException if role is null
     */
    Role create(Role role);

    /**
     * Updated role
     * @param role to update
     * @return updated role
     * @throws NullEntityReferenceException if role is null
     * @throws EntityNotFoundException if role with this id is null
     */
    Role update(Role role);

    /**
     * Find role
     * @param id of role
     * @return role
     * @throws EntityNotFoundException if there is no role with such id
     */
    Role readById(long id);

    /**
     * Find Role by name
     * @param name of role
     * @return role
     * @throws EntityNotFoundException if there is no role with this name
     */
    Role readByName(String name);



    /**
     * Delete role
     * @param id of role
     * @throws EntityNotFoundException if there is no role with such id
     */
    void delete(long id);


    /**
     * Get all roles
     * @return List with roles
     */
    List<Role> getAll();

}
