package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.UserBlock;

import javax.persistence.EntityNotFoundException;


/**
 * User Block Service
 * @author Arsen Sydoryk
 */
public interface UserBlockService {

    /**
     * Create ban
     * @param block to create
     * @return created block
     * @throws NullEntityReferenceException if block is null
     */
    UserBlock create(UserBlock block);

    /**
     * Update ban
     * @param block to update
     * @return updated block
     * @throws NullEntityReferenceException if block is null
     * @throws EntityNotFoundException if block with this id is null
     */
    UserBlock update(UserBlock block);


    /**
     * Read block
     * @param id of block
     * @return block
     * @throws EntityNotFoundException if block with this id is null
     */
    UserBlock readById(long id);


    /**
     * Read block by user id
     * @param userId of user
     * @return block
     * @throws EntityNotFoundException if block with this id is null
     */
    UserBlock readByUserId(long userId);

    /**
     * Delete block
     * @param id of block
     * @throws EntityNotFoundException if block with this id is null
     */
    void delete(long id);

}
