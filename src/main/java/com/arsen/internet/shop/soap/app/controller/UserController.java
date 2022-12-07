package com.arsen.internet.shop.soap.app.controller;

import com.arsen.internet.shop.soap.app.dto.user.UserEditDto;
import com.arsen.internet.shop.soap.app.dto.user.UserPasswordChangeDto;
import com.arsen.internet.shop.soap.app.dto.user.UserTopUpDto;
import com.arsen.internet.shop.soap.app.model.Image;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.repository.ImageRepository;
import com.arsen.internet.shop.soap.app.service.UserService;
import com.arsen.internet.shop.soap.app.utils.Generator;
import com.arsen.internet.shop.soap.app.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;


/**
 * User controller
 * @author Arsen Sydoryk
 */
@Controller
@PreAuthorize("isAuthenticated()")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Show user's profile
     * @param authentication to get principal (user)
     * @param model model
     * @return view
     */
    @GetMapping(value = {"/profile"})
    public String profile(Authentication authentication, Model model){
        log.trace("Setting user to attribute");
        model.addAttribute("user", authentication.getPrincipal());
        return "profile";
    }


    /**
     * Top up page
     * @param model model
     * @return view
     */
    @GetMapping("/topup")
    public String topUp(Model model){
        log.trace("Setting Ussr Top Up DTO to attribute");
        model.addAttribute("topUp", new UserTopUpDto());
        return "topup";
    }


    /**
     * Top up
     * @param topUp dto with sum
     * @param errors Errors
     * @return redirect/view (errors)
     */
    @PostMapping("/topup")
    public String topUp(@Valid @ModelAttribute("topUp") UserTopUpDto topUp, Errors errors){

        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error -> log.trace("Error code: " + error.getDefaultMessage()));
            return "topup";
        }

        log.trace("Loading user from SESSION");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.trace("[" + user.getId() + "] Changing balance...");
        user.setBalance(user.getBalance() + topUp.getMoney());
        log.trace("[" + user.getId() + "] New balance: " + user.getBalance());

        log.trace("Updating user with id " + user.getId());
        userService.update(user);
        return "redirect:/profile";

    }


    /**
     * Edit profile page
     * @param model model
     * @return view
     */
    @GetMapping("/edit")
    public String edit(Model model){
        log.trace("Loading user from SESSION");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.trace("Setting Ussr Edit DTO to attribute");
        model.addAttribute("user", new UserEditDto(user));
        return "edit";
    }


    /**
     * Edit profile
     * @param edit dto with params
     * @param errors Errors
     * @param avatar new avatar
     * @param model model
     * @return redirect or view
     * @throws IOException
     */
    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("user") UserEditDto edit, Errors errors,
                       @RequestParam(name = "avatar", required = false) MultipartFile avatar, Model model) throws IOException {

        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error -> log.trace("Error code: " + error.getDefaultMessage()));
            model.addAttribute("user", edit);
            return "edit";
        }

        log.trace("Loading user from SESSION");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setFirstName(edit.getFirstName());
        user.setLastName(edit.getLastName());
        user.setBirthDate(Date.valueOf(edit.getBirthDate()));

        log.trace("[" + user.getId() + "] User's first name: " + user.getFirstName());
        log.trace("[" + user.getId() + "] User's last name: " + user.getLastName());
        log.trace("[" + user.getId() + "] User's birthdate: " + user.getBirthDate());


        log.trace("Checking if user wants to update/create image");
        if(avatar != null && !avatar.isEmpty() && avatar.getSize() > 0){
            log.trace("Yes, he actually wants");
            Image image = user.getImage();
            if(image == null){
                log.trace("Creating new image...");
                image = Image.builder()
                        .uuid(Generator.getRandomString())
                        .build();
            }

            log.trace("Set image params...");
            image.setContentType(avatar.getContentType());
            image.setData(ImageUtil.compress(avatar.getBytes()));

            image = imageRepository.save(image);
            log.trace("Creating/updating image with id " + image.getId());
            user.setImage(image);

        }

        log.trace("Updating user " + user.getId());
        userService.update(user);
        return "redirect:/profile";
    }


    /**
     * Edit password page
     * @param model model
     * @return view
     */
    @GetMapping("/edit/password")
    public String editPassword(Model model){
        log.trace("Setting Ussr Password Change DTO to attribute");
        model.addAttribute("user", new UserPasswordChangeDto());
        return "password_change";
    }


    /**
     * Edit password
     * @param passwordChangeDto dto with passwords
     * @param errors Errors
     * @return redirect or view
     */
    @PostMapping("/edit/password")
    public String editPassword(@Valid @ModelAttribute("user") UserPasswordChangeDto passwordChangeDto, Errors errors){

        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error -> log.trace("Error code: " + error.getDefaultMessage()));
            return "password_change";
        }

        log.trace("Loading user from SESSION");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.trace("Setting new password...");
        user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));

        log.trace("Updating user " + user.getId());
        userService.update(user);

        return "redirect:/profile";
    }
}
