package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.model.UserBlock;
import org.springframework.data.domain.Page;

import javax.persistence.EntityNotFoundException;
import java.util.List;


/**
 * User Service
 * @author Arsen Sydoryk
 */
public interface UserService {


    /**
     * Create user
     * @param user to create
     * @return created user
     * @throws NullEntityReferenceException if user is null
     */
    User create(User user);

    /**
     * Update user
     * @param user to update
     * @return updated user
     * @throws NullEntityReferenceException if user is null
     * @throws EntityNotFoundException if user with this id is null
     */
    User update(User user);

    /**
     * Read user
     * @param id of user
     * @return user
     * @throws EntityNotFoundException if user with this id is null
     */
    User readById(long id);


    /**
     * Read user by email
     * @param email String
     * @return user
     * @throws EntityNotFoundException if user with this email is null
     */
    User readByEmail(String email);

    /**
     * Delete user
     * @param userId of user
     * @throws EntityNotFoundException if user with this id is null
     */
    void delete(long userId);


    /**
     * Get all users
     * @return List of Users
     */
    List<User> getAll();

    /**
     * Get users by page
     * @param page number
     * @return Page with Users
     */
    Page<User> getAll(int page);


    /**
     * Get baneed users
     * @param page number
     * @return Page with UserBlocks
     */
    Page<UserBlock> getAllBanned(int page);

}
