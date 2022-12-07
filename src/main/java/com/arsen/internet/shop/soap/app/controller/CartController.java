package com.arsen.internet.shop.soap.app.controller;

import com.arsen.internet.shop.soap.app.model.Cart;
import com.arsen.internet.shop.soap.app.model.Product;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.model.data.CartStatus;
import com.arsen.internet.shop.soap.app.service.CartService;
import com.arsen.internet.shop.soap.app.service.ProductService;
import com.arsen.internet.shop.soap.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.util.Date;


/**
 * Cart controller
 * @author Arsen Sydoryk
 */
@Controller
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;


    /**
     * Root page of cart
     * @return view
     */
    @GetMapping({"/", ""})
    public String index(){
        log.trace("Returning default cart page...");
        return "cart/cart";
    }


    /**
     * Add new product to cart
     * @param id of product
     * @param quantity of product
     * @param model model
     * @return redirect to list
     */
    @PostMapping("/add/{id}")
    public String add(@PathVariable long id, @RequestParam(value = "quantity") long quantity,  Model model){

        //TODO: Think about DTO and proper validation
        log.trace("Reading product with id " + id + "...");
        Product product = productService.readById(id);
        log.trace("Checking declared quantity, comparing with product's total quantity");
        if(quantity > product.getQuantity() || quantity <= 0){
            log.error("Error with code: cart.product.add.invalid.amount.enough");
            model.addAttribute("message", "cart.product.add.invalid.amount.enough");
            return "product/product_show";
        }

        log.trace("Getting user from SESSION...");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.trace("Creating new cart");
        log.trace("Product id: " + id);
        log.trace("Quantity: " + quantity);
        log.trace("User id: " + user.getId());

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setActualPrice(product.getPrice() * quantity);
        cart.setDate(Date.from(Instant.now()));
        cart.setStatus(CartStatus.CARTED);
        cart.setQuantity(quantity);

        log.trace("Creating new cart in DB");
        cart = cartService.create(cart);
        log.trace("Created new cart with id " + cart.getId());

        return "redirect:/cart/carted";
    }


    /**
     * Buy product
     * @param id of cart
     * @param redirectAttributes with errors
     * @return redirect
     */
    @GetMapping("/buy/{id}")
    @PreAuthorize("@cartController.checkCart(#id)")
    public String buy(@PathVariable long id, RedirectAttributes redirectAttributes){

        //TODO: Think about DTO and proper validation
        log.trace("Reading cart with id " + id + "...");
        Cart cart = cartService.readById(id);

        if(cart.getStatus() != CartStatus.CARTED){
            log.error("Error code: buy.cart.product.buy.error.cart");
            redirectAttributes.addFlashAttribute("message", "buy.cart.product.buy.error.cart");
            return "redirect:/cart/carted";
        }

        log.trace("Loading product from cart...");
        Product product = cart.getProduct();
        log.trace("Getting user from SESSION...");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(product.getQuantity() < cart.getQuantity()){
            log.error("Error code: buy.cart.product.buy.error.quantity");
            redirectAttributes.addFlashAttribute("message", "buy.cart.product.buy.error.quantity");
            return "redirect:/cart/carted";
        }

        if(user.getBalance() < cart.getPrice()){
            log.error("Error code: buy.cart.product.buy.error.money");
            redirectAttributes.addFlashAttribute("message", "buy.cart.product.buy.error.money");
            return "redirect:/cart/carted";
        }


        log.trace("Changing product total quantity");
        product.setQuantity(product.getQuantity() - cart.getQuantity());
        log.trace("New product(" + product.getId() + ") quantity: " + product.getQuantity());
        log.trace("Updating product");
        product = productService.update(product);


        cart.setActualPrice(cart.getPrice());
        cart.setProduct(product);
        cart.setStatus(CartStatus.BOUGHT);

        log.trace("User id: " + user.getId());
        log.trace("[" + cart.getId() + "] Actual price: " + cart.getActualPrice());
        log.trace("[" + cart.getId() + "] Status: " + cart.getStatus().name());

        user.setBalance(user.getBalance() - cart.getPrice());
        log.trace("[" + user.getBalance() + "] User balance: " + user.getBalance());

        log.trace("Updating user...");
        userService.update(user);
        log.trace("Updating cart...");
        cartService.create(cart);

        return "redirect:/cart/bought";

    }


    /**
     * Show list of products in cart
     * @param page number
     * @param type status
     * @param error message
     * @param model model
     * @return view
     */
    @GetMapping("/{type}")
    public String cartShow(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                         @PathVariable("type") String type,
                         @ModelAttribute(value = "message", binding = false) String error,
                         Model model){

        log.trace("Getting user from SESSION...");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.trace("Loading page with parts");
        Page<Cart> carts = cartService.getByUserId(user.getId(), CartStatus.valueOf(type.toUpperCase()), page);

        if(!error.isEmpty()) {
            log.error("Error code: " + error);
        }

        model.addAttribute("message", error);
        log.trace("Setting carts to model attribute");
        model.addAttribute("cart_list", carts.getContent());
        log.trace("Setting page to model attribute (" + page + ")");
        model.addAttribute("page", page);
        log.trace("Setting total pages to model attribute (" + carts.getTotalPages() + ")");
        model.addAttribute("pages", carts.getTotalPages());
        log.trace("Setting status(type) to model attribute (" + type + ")");
        model.addAttribute("type", type);

        return "cart/cart_list";
    }


    /**
     * Cancel order
     * @param id of cart
     * @param redirectAttributes with errors
     * @return redirect
     */
    @GetMapping("/cancel/{id}")
    @PreAuthorize("@cartController.checkCart(#id)")
    public String cancel(@PathVariable long id, RedirectAttributes redirectAttributes){

        //TODO: CREATE DTO WITH VALIDATION
        log.trace("Loading cart by id " + id);
        Cart cart = cartService.readById(id);
        if(cart.getStatus() == CartStatus.CARTED){
            log.error("Error code: cancel.cart.product.error.cart");
            redirectAttributes.addFlashAttribute("message", "cancel.cart.product.error.cart");
            return "redirect:/cart/bought";
        }

        if(cart.getStatus() == CartStatus.CANCELLED){
            log.error("Error code: cancel.cart.product.error.cancel");
            redirectAttributes.addFlashAttribute("message", "cancel.cart.product.error.cancel");
            return "redirect:/cart/bought";
        }

        log.trace("Loading user from SESSION");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.trace("Loading product from cart (" + cart.getId() + ")");
        Product product = cart.getProduct();

        log.trace("Changing user balance (user id = " + user.getId() + ")");
        user.setBalance(user.getBalance() + cart.getPrice());
        log.trace("User balance: " + user.getBalance());

        cart.setStatus(CartStatus.CANCELLED);

        log.trace("Updating cart (" + id + ")");
        cartService.update(cart);

        log.trace("Updating product total quantity");
        product.setQuantity(product.getQuantity() + cart.getQuantity());
        log.trace("[" + product.getId() + "] Product total quantity: " + product.getQuantity());

        log.trace("Updating product...");
        productService.update(product);
        log.trace("Updating user...");
        userService.update(user);

        return "redirect:/cart/cancelled";
    }


    /**
     * Delete order (not bought) in cart
     * @param id of cart
     * @param redirectAttributes with errors
     * @return redirect
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("@cartController.checkCart(#id)")
    public String delete(@PathVariable long id, RedirectAttributes redirectAttributes){

        log.trace("Loading cart with id " + id);
        Cart cart = cartService.readById(id);
        if(cart.getStatus() != CartStatus.CARTED){
            log.error("Error code: delete.cart.product.error.purchased");
            redirectAttributes.addFlashAttribute("message", "delete.cart.product.error.purchased");
            return "redirect:/cart/carted";
        }

        log.trace("Deleting cart with id " + id + "...");
        cartService.delete(cart.getId());
        return "redirect:/cart/carted";
    }


    /**
     * Method to check cart owner
     * @param cartId of cart
     * @return true if current user's owner of cart, otherwise false
     */
    public boolean checkCart(long cartId){

        log.trace("[SECURITY] Loading cart with id " + cartId);
        Cart cart = cartService.readById(cartId);
        log.trace("[SECURITY] Loading user from SESSSION");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.trace("[SECURITY] Compare owner id and user id");
        return cart.getUser().getId() == user.getId();

    }

}
