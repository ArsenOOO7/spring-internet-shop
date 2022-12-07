package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.Cart;
import com.arsen.internet.shop.soap.app.model.data.CartStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Cart repository
 * @author Arsen Sydoryk
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {


    /**
     * Get carts by status and user id
     * @param userId of user
     * @param status of cart
     * @param pageable page
     * @return Page with carts
     */
    Page<Cart> getCartsByUserIdAndStatus(long userId, CartStatus status, Pageable pageable);

}
