package com.arsen.internet.shop.soap.app.controller;

import com.arsen.internet.shop.soap.app.dto.user.UserBanDto;
import com.arsen.internet.shop.soap.app.model.Cart;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.model.UserBlock;
import com.arsen.internet.shop.soap.app.model.data.CartStatus;
import com.arsen.internet.shop.soap.app.service.CartService;
import com.arsen.internet.shop.soap.app.service.UserBlockService;
import com.arsen.internet.shop.soap.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;


/**
 * Admin controller (panel)
 * @author Arsen Sydoryk
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserBlockService blockService;

    @Autowired
    private CartService cartService;


    /**
     * Show all users.
     * @param page number
     * @param search name TODO
     * @param banned flag
     * @param model Model
     * @return view
     */
    @GetMapping("/users")
    public String users(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "banned", required = false, defaultValue = "false") boolean banned,
            Model model){

        //TODO: Create Criteria Builder for this & implement search by name|surname

        List<User> userList;
        long total;

        log.trace("Getting list of users on specific page...");
        if(banned){
            log.trace("Type: banned users");
            Page<UserBlock> userBlocks = userService.getAllBanned(page);
            total = userBlocks.getTotalPages();
            userList = userBlocks.stream().map(UserBlock::getUser).toList();
        }else{
            log.trace("Type: all users");
            Page<User> users = userService.getAll(page);
            total = users.getTotalPages();
            userList = users.getContent();
        }

        log.trace("Total pages: " + total);

        log.trace("Setting total pages to model attribute");
        model.addAttribute("pages", total);
        log.trace("Setting current to model attribute");
        model.addAttribute("page", page);
        log.trace("Setting users to model attribute");
        model.addAttribute("users", userList);


        log.debug("Building query...");
        StringBuilder builder = new StringBuilder();
        if(!search.isEmpty()){
            builder.append("search").append("=").append(search);
        }

        if(!banned){
            if(builder.length() > 0){
                builder.append("&");
            }
            builder.append("banned").append("=").append("true");
        }

        if(builder.length() > 0){
            builder.setCharAt(0, '&');
        }

        log.trace("Adding query line to model attribute");
        model.addAttribute("query", builder.toString());

        return "admin/admin_users";

    }


    /**
     * Show specific user
     * @param id of user
     * @param model model
     * @return view
     */
    @GetMapping("/users/{id}")
    public String show(@PathVariable long id, Model model){
        log.trace("Looking for user with id " + id + "...");
        User user = userService.readById(id);
        log.trace("Setting user to model attribute...");
        model.addAttribute("user", user);
        return "admin/admin_user_show";
    }


    /**
     * Show page of ban
     * @param userId of user
     * @param model model
     * @return view
     */
    @GetMapping("/users/ban/{userId}")
    public String ban(@PathVariable long userId, Model model){
        log.trace("Looking for user with id " + userId + "...");
        log.trace("Setting UserBan DTO to model attribute");
        model.addAttribute("ban", new UserBanDto(userService.readById(userId).getId()));
        return "admin/admin_ban";
    }


    /**
     * Ban user
     * @param userId of user
     * @param ban dto
     * @return view
     */
    @PostMapping("/users/ban/{userId}")
    public String ban(@PathVariable long userId, @ModelAttribute("ban") UserBanDto ban){

        log.trace("Looking for user with id " + userId + "...");
        User user = userService.readById(userId);

        log.trace("Extracting UserBlock from user");
        UserBlock userBlock = user.getBlock();

        if(userBlock == null){
            log.trace("UserBlock is null, creating new...");
            userBlock = new UserBlock();
            userBlock.setUser(user);
        }

        log.trace("Getting current date in unix");
        long now = Instant.now().getEpochSecond();


        userBlock.setReason(ban.getReason());
        userBlock.setStartTime(now);
        userBlock.setEndTime(now + (ban.getEndHours() * 60 * 60));

        log.trace("Reason: " + userBlock.getReason());
        log.trace("Start time: " + userBlock.getStartTime());
        log.trace("End time: " + userBlock.getEndTime());

        log.trace("Saving ban to DB,..");
        blockService.create(userBlock); //<---- ONLY!!!

        return "redirect:/admin/users/" + userId;
    }


    /**
     * Unban user
     * @param userId of user
     * @return redirect
     */
    @GetMapping("/users/unban/{userId}")
    public String unBan(@PathVariable long userId){
        log.trace("Looking for user with id " + userId + "...");
        User user = userService.readById(userId);

        log.trace("Extracting UserBlock from user");
        UserBlock userBlock = user.getBlock();
        if(userBlock != null){
            log.trace("Removing ban...");
            blockService.delete(userBlock.getId());
        }

        return "redirect:/admin/users/" + userId;
    }


    /**
     * Show cart of specific user
     * @param userId of user
     * @param type of cart (status)
     * @param page number
     * @param model model
     * @return view
     */
    @GetMapping("/cart/{userId}/{type}")
    public String cart(@PathVariable long userId, @PathVariable String type,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       Model model){

        log.trace("Looking for user with id " + userId + "...");
        User user = userService.readById(userId);
        log.trace("Getting list of carts on page " + page + ". Type: " + type);
        Page<Cart> cartPages = cartService.getByUserId(userId, CartStatus.valueOf(type.toUpperCase()), page);

        log.trace("Setting list of carts to attribute...");
        model.addAttribute("carts", cartPages.getContent());
        log.trace("Setting status (type) of cart to attribute...");
        model.addAttribute("type", type);
        log.trace("Setting current page to attribute...");
        model.addAttribute("page", page);
        log.trace("Setting total pages to attribute...");
        model.addAttribute("pages", cartPages.getTotalPages());

        return "admin/admin_cart";

    }


    /**
     * Update user's cart
     * @param cartId of cart
     * @param status new
     * @return redirect
     */
    @PostMapping("/cart/edit/{cartId}")
    public String cartEdit(@PathVariable long cartId, @RequestParam("status") String status){

        log.trace("Loading cart with id " + cartId + "...");
        Cart cart = cartService.readById(cartId);
        log.trace("Changing status to " + status + "...");
        cart.setStatus(CartStatus.valueOf(status.toUpperCase()));

        log.trace("Updating cart...");
        cartService.update(cart);

        return "redirect:/admin/cart/" + cart.getUser().getId() + "/" + cart.getStatus().name().toLowerCase();
    }

}
