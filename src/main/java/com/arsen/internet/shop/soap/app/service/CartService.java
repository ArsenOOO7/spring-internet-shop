package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Cart;
import com.arsen.internet.shop.soap.app.model.data.CartStatus;
import org.springframework.data.domain.Page;

import javax.persistence.EntityNotFoundException;
import java.util.List;


/**
 * Cart Service
 * @author Arsen Sydoryk
 */
public interface CartService {


    /**
     * Create new cart
     * @param cart cart
     * @return created cart
     * @throws NullEntityReferenceException if cart is null
     */
    Cart create(Cart cart);

    /**
     * Update new cart
     * @param cart cart
     * @return updated cart
     * @throws EntityNotFoundException if there is no cart with such id
     * @throws NullEntityReferenceException if cart is null
     */
    Cart update(Cart cart);

    /**
     * Find cart
     * @param id of cart
     * @return cart
     * @throws EntityNotFoundException if cart with this id is not found
     */
    Cart readById(long id);


    /**
     * Delete cart
     * @param id of cart
     * @throws EntityNotFoundException if cart with this id is not found
     */
    void delete(long id);


    /**
     * Get all carts
     * @return List of Carts
     */
    List<Cart> getAll();


    /**
     * Get all carts by user id
     * @param userId of user
     * @param status of cart
     * @param page number
     * @return Page with Carts
     */
    Page<Cart> getByUserId(long userId, CartStatus status, int page);

}
