package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.*;
import com.arsen.internet.shop.soap.app.model.data.CartStatus;
import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.SizeUnit;
import com.arsen.internet.shop.soap.app.utils.ShopValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void checkAll(){

        Category category = new Category();
        category.setIdentifier("test");
        category.setLocaleEn("Test");
        category.setLocaleUa("Тест");
        category = categoryRepository.save(category);


        Role role = roleRepository.readRoleByName("ADMIN");
        User user = new User();
        user.setFirstName("Arsen");
        user.setLastName("Second");
        user.setEmail("arsen.sydoryk@gmail.com");
        user.setLogin("ArsenOOO8");
        user.setPassword("mySTRONGpassword123");
        user.setBirthDate(Date.valueOf("2004-03-15"));
        user.setBalance(100.0);

        user.setRole(role);
        user = userRepository.save(user);

        List<Product> products = getProducts(category);
        products = productRepository.saveAll(products);

        for(Product product: products){

            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(10);
            cart.setDate(Date.from(Instant.now()));
            cart.setStatus(CartStatus.CARTED);
            cart.setActualPrice(0);
            cartRepository.save(cart);

        }

        Page<Cart> page = cartRepository.getCartsByUserIdAndStatus(user.getId(), CartStatus.CARTED, PageRequest.of(0, ShopValues.MAX_ROWS));

        List<Cart> carts = page.getContent();
        assertEquals(ShopValues.MAX_ROWS, carts.size());

        Cart cart = carts.get(0);

        double oldPrice = cart.getQuantity() * cart.getProduct().getPrice();
        double actualPrice = cart.getPrice();

        assertEquals(oldPrice, actualPrice);

        for(int i = 0; i < counter / 2; ++i){
            cart = carts.get(i);
            cart.setActualPrice(cart.getPrice());
            cart.setStatus(CartStatus.BOUGHT);
        }

        cartRepository.saveAll(carts);
        page = cartRepository.getCartsByUserIdAndStatus(user.getId(), CartStatus.CARTED, PageRequest.of(0, ShopValues.MAX_ROWS));
        carts = page.getContent();

        assertEquals(counter / 2, carts.size());

        page = cartRepository.getCartsByUserIdAndStatus(user.getId(), CartStatus.BOUGHT, PageRequest.of(0, ShopValues.MAX_ROWS));
        carts = page.getContent();

        assertEquals(counter / 2, carts.size());

        cart = carts.get(0);
        oldPrice = cart.getQuantity() * cart.getProduct().getPrice();
        actualPrice = cart.getPrice();

        assertEquals(oldPrice, actualPrice);

        Product product = products.get(0);
        product.setPrice(product.getPrice() * 2);

        productRepository.save(product);
        product = productRepository.findById(product.getId()).orElse(null);
        cart = cartRepository.findById(carts.get(0).getId()).orElse(null);

        assertNotNull(cart);
        assertNotNull(product);
        assertNotEquals(cart.getPrice(), product.getPrice() * cart.getQuantity());
        assertEquals(1, product.getBoughtCounter());

        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.delete(user);
        categoryRepository.delete(category);


    }


    private static final int counter = 10;
    private static List<Product> getProducts(Category category){
        List<Product> products = new LinkedList<>();
        Product product;
        for(int i = 0; i < counter; ++i){
            product = new Product();
            product.setShortName("Test " + i);
            product.setFullName("Full name test " + i);
            product.setDescription("Description " + i);
            product.setQuantity(100 + i);
            product.setPrice(100.0 + i);
            product.setCategory(category);
            product.setColor(Color.BLACK);
            product.setSizeUnit(SizeUnit.CM);
            product.setSizeValue(100);
            product.setCreatedAt(java.util.Date.from(Instant.now()));
            products.add(product);
        }

        return products;
    }

}
