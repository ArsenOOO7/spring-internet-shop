package com.arsen.internet.shop.soap.app.conf.provider;

import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Authentication provider
 * @author Arsen Sydoryk
 */
@Slf4j
@Component
public class ShopAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder encoder;
    private final UserRepository repository;

    public ShopAuthenticationProvider(PasswordEncoder encoder, UserRepository repository) {
        this.encoder = encoder;
        this.repository = repository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = repository.findByEmail(email);
        if(user != null && encoder.matches(password, user.getPassword())){
            log.trace("New user signed in (" + user.getFirstName() + " " + user.getLastName() + "; ID: " + user.getId() + ")");
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }

        throw new BadCredentialsException("Invalid login or password!");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
