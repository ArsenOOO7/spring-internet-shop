package com.arsen.internet.shop.soap.app.controller;

import com.arsen.internet.shop.soap.app.dto.user.UserRegisterDto;
import com.arsen.internet.shop.soap.app.dto.user.UserTransformer;
import com.arsen.internet.shop.soap.app.model.Image;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.repository.ImageRepository;
import com.arsen.internet.shop.soap.app.service.UserService;
import com.arsen.internet.shop.soap.app.utils.Generator;
import com.arsen.internet.shop.soap.app.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;


/**
 * Authentication controller
 * @author Arsen Sydoryk
 */
@Controller
@PreAuthorize("isAnonymous()")
@Slf4j
public class LoginController {


    @Autowired
    private UserService userService;

    @Autowired
    private ImageRepository imageRepository;


    /**
     * Login page
     * @param error true if exception was thrown
     * @param model model
     * @return view
     */
    @RequestMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "banned", required = false) String banned,
            @RequestParam(value = "reason", required = false) String reason,
            @RequestParam(value = "end_time", required = false) String endTime,
            Model model){

        if(error != null){
            log.error("Error code: user.input.login.undefined");
            model.addAttribute("message", "user.input.login.undefined");
        }

        if(banned != null){
            model.addAttribute("reason", reason);
            model.addAttribute("end_time", endTime);
        }


        return "login";
    }

    /**
     * Sign up page
     * @param model model
     * @return view
     */
    @GetMapping("/register")
    public String register(Model model){
        log.trace("Setting UserRegister DTO to attribute");
        model.addAttribute("user", new UserRegisterDto());
        return "register";
    }


    /**
     * Sign up
     * @param userRegisterDto user
     * @param errors Errors
     * @return redirect or view (if there are any errors)
     * @throws IOException
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegisterDto userRegisterDto, Errors errors) throws IOException {

        if(errors.hasErrors()){
            errors.getAllErrors().forEach(error -> log.trace("Error code: " + error.getDefaultMessage()));
            return "register";
        }


        log.trace("Converting DTO to User entity...");
        User user = UserTransformer.registerDtoToUser(userRegisterDto);

        log.trace("Getting Multipart File...");
        MultipartFile avatar = userRegisterDto.getAvatar();

        log.trace("Checking if multipart file != null");
        if(avatar != null && !avatar.isEmpty() && avatar.getSize() > 0) {
            log.trace("Multipart file is not null!");

            Image image = imageRepository.save(Image.builder()
                    .uuid(Generator.getRandomString())
                    .contentType(avatar.getContentType())
                    .data(ImageUtil.compress(avatar.getBytes())).build()
            );

            log.trace("Created new image with id " + image.getId());
            log.trace("[" + image.getId() + "] Image UUID: " + image.getUuid());
            log.trace("[" + image.getId() + "] Image content=type: " + image.getContentType());

            log.trace("Setting image to user");
            user.setImage(
              image
            );
        }

        log.trace("Creating new user...");
        user = userService.create(user);
        log.trace("New user with id " + user.getId() + " successfully created!");

        log.trace("[" + user.getId() + "] User's first name: " + user.getFirstName());
        log.trace("[" + user.getId() + "] User's last name: " + user.getLastName());
        log.trace("[" + user.getId() + "] User's email: " + user.getEmail());
        log.trace("[" + user.getId() + "] User's login: " + user.getLogin());
        log.trace("[" + user.getId() + "] User's birthdate: " + user.getBirthDate());


        log.trace("Saving new user in SESSION");
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        return "redirect:/";
    }
}
