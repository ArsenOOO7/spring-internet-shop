package com.arsen.internet.shop.soap.app.service;


import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Cart;
import com.arsen.internet.shop.soap.app.model.Product;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.model.data.CartStatus;
import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.SizeUnit;
import com.arsen.internet.shop.soap.app.repository.CartRepository;
import com.arsen.internet.shop.soap.app.service.impl.CartServiceImpl;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CartServiceTest {

    @MockBean
    private CartRepository cartRepository;

    @Autowired
    private CartServiceImpl cartService;


    private static User user;
    private static Product product;
    private static Cart cart;

    @BeforeAll
    public static void beforeAll() {

        user = new User();
        user.setId(1);
        user.setFirstName("Nick");
        user.setLastName("Green");
        user.setEmail("nick@mail.com");
        user.setPassword("2222");

        product = new Product();

        product.setShortName("Product");
        product.setFullName("Product full name");
        product.setDescription("Some description");
        product.setQuantity(100);
        product.setPrice(100.0);
        product.setColor(Color.BLACK);
        product.setSizeUnit(SizeUnit.MM);
        product.setSizeValue(1000);


        cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(10);
        cart.setStatus(CartStatus.CARTED);
        cart.setActualPrice(0);

    }

    @Test
    public void createNullCartTest(){

        assertThrows(NullEntityReferenceException.class, () -> cartService.create(null));

    }


    @Test
    public void createNormalCartTest(){

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        assertEquals(cart, cartService.create(cart));

    }

    @Test
    public void updateCartAllCases(){
        assertThrows(NullEntityReferenceException.class, () -> cartService.update(null));

        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> cartService.update(cart));

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(ans -> ans.getArgument(0));

        assertEquals(cart, cartService.update(cart));
    }


    @Test
    public void checkReadingCart(){
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> cartService.readById(0));

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        assertEquals(cart, cartService.readById(1L));
    }


    @Test
    public void checkDeleteCart(){
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> cartService.delete(1));

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        assertDoesNotThrow(() -> cartService.delete(1));
    }


    @Test
    public void checkGetAll(){
        when(cartRepository.findAll()).thenReturn(List.of(cart, cart));
        assertEquals(List.of(cart, cart), cartService.getAll());
    }


    @Test
    public void checkPage(){

        Page<Cart> page = mock(Page.class);
        when(page.getContent()).thenReturn(List.of(cart, cart));
        when(page.getTotalPages()).thenReturn(1);

        when(cartRepository.getCartsByUserIdAndStatus(anyLong(), any(CartStatus.class), any(Pageable.class))).thenReturn(page);
        Page<Cart> result = cartService.getByUserId(user.getId(), CartStatus.CARTED, 1);
        assertEquals(List.of(cart, cart), result.getContent());
        assertEquals(1, result.getTotalPages());

    }

}
