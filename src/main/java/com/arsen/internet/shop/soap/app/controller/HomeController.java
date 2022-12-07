package com.arsen.internet.shop.soap.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Home controller, home page
 * @author Arsen Sydoryk
 */
@Controller
public class HomeController {


    /**
     * Home page
     * @return view
     */
    @RequestMapping({"/home", "/", ""})
    public String home(){
        return "home";
    }
}
