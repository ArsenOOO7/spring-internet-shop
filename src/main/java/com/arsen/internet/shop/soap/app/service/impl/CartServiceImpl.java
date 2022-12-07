package com.arsen.internet.shop.soap.app.service.impl;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Cart;
import com.arsen.internet.shop.soap.app.model.data.CartStatus;
import com.arsen.internet.shop.soap.app.repository.CartRepository;
import com.arsen.internet.shop.soap.app.service.CartService;
import com.arsen.internet.shop.soap.app.utils.ShopValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart create(Cart cart) {
        if(cart == null){
            throw new NullEntityReferenceException("Cart cannot be null!");
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart update(Cart cart) {
        if(cart == null){
            throw new NullEntityReferenceException("Cart cannot be null!");
        }

        readById(cart.getId());
        return cartRepository.save(cart);
    }

    @Override
    public Cart readById(long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id " + id + " is not found!"));
    }

    @Override
    public void delete(long id) {
        cartRepository.delete(readById(id));
    }

    @Override
    public List<Cart> getAll() {
        return cartRepository.findAll();
    }

    @Override
    public Page<Cart> getByUserId(long userId, CartStatus status, int page) {
        return cartRepository.getCartsByUserIdAndStatus(userId, status, PageRequest.of(page - 1, ShopValues.MAX_ROWS));
    }
}
