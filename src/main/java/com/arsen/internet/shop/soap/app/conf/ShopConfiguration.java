package com.arsen.internet.shop.soap.app.conf;

import com.arsen.internet.shop.soap.app.conf.auth.ShopAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


/**
 * Configuration. Security, localization...
 * @author Arsen Sydoryk
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ShopConfiguration implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests()
                .mvcMatchers("/product", "/product/show/**").permitAll()
                .mvcMatchers("/home", "/", "/index").permitAll()
                .mvcMatchers("/login").anonymous()
                .mvcMatchers("/register").anonymous()
                .anyRequest().authenticated();

        httpSecurity.formLogin()
                .loginPage("/login")
                .successHandler(new ShopAuthenticationSuccessHandler()).permitAll();

        httpSecurity.logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true).permitAll();

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        return httpSecurity.build();
    }


    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor changeInterceptor = new LocaleChangeInterceptor();
        changeInterceptor.setParamName("lang");
        return changeInterceptor;
    }

    @Bean
    public LocaleResolver localeResolver(){
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setCookieName("language");
        cookieLocaleResolver.setCookieMaxAge(24 * 60 * 60 * 1000);
        return cookieLocaleResolver;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(localeChangeInterceptor());
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
