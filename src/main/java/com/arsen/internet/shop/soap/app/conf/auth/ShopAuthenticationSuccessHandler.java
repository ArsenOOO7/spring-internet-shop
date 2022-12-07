package com.arsen.internet.shop.soap.app.conf.auth;

import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.model.UserBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Filter after successful authentication. Checks if user's not banned
 * @author Arsen Sydoryk
 */

@Slf4j
public class ShopAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.trace("Loading user from SESSION");
        User user = (User) authentication.getPrincipal();
        if(user.getBlock() != null && user.getBlock().isBanned()){
            log.trace("User("+ user.getId() + ") is banned...");
            UserBlock userBlock = user.getBlock();

            log.trace("Reason: " + userBlock.getReason());
            log.trace("End time: " + userBlock.getEndTime());

            response.sendRedirect("/login?banned=true&reason=" + userBlock.getReason() + "&end_time=" + convertToDate(userBlock.getEndTime()));
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            return;
        }

        log.trace("Ok, he can continue...");
        response.sendRedirect("/");

    }


    private String convertToDate(long seconds){
        return new SimpleDateFormat("HH:mm:ss MM-dd-yyyy").format(new Date(seconds * 1000L));
    }
}
