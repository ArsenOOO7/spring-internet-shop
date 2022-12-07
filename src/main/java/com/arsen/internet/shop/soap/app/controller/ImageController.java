package com.arsen.internet.shop.soap.app.controller;

import com.arsen.internet.shop.soap.app.model.Image;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.repository.ImageRepository;
import com.arsen.internet.shop.soap.app.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;


/**
 * Image controller
 * @author Arsen Sydoryk
 */
@Controller
@RequestMapping("/image")
@Slf4j
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;


    /**
     * Show image
     * @param id of image
     * @param user true if it is user's image
     * @return body with image
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> shop(@PathVariable long id, @RequestParam(name = "user", required = false) boolean user){

        Image image;
        if(!user) {
            log.trace("Loading image with id " + id + "...");
            image = imageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Image with id " + id + " not found!"));
        }else{
            image = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getImage();
            log.trace("Loading image from user. Image id: " + image.getId());
        }

        log.trace("Forming response body....");
        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(image.getContentType()))
                .body(ImageUtil.decompress(image.getData()));

    }

}
