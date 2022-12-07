package com.arsen.internet.shop.soap.app.dto;

import com.arsen.internet.shop.soap.app.InternetShopApplication;
import com.arsen.internet.shop.soap.app.dto.user.UserEditDto;
import com.arsen.internet.shop.soap.app.dto.user.UserPasswordChangeDto;
import com.arsen.internet.shop.soap.app.dto.user.UserRegisterDto;
import com.arsen.internet.shop.soap.app.dto.user.UserTopUpDto;
import com.arsen.internet.shop.soap.app.model.User;
import com.arsen.internet.shop.soap.app.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InternetShopApplication.class)
@ActiveProfiles("test")
public class UserDtoTest {

    @Autowired
    private Validator validator;

    @Autowired
    private RoleRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void checkUserIncorrectTopUp(){

        UserTopUpDto topUpDto = new UserTopUpDto();
        topUpDto.setMoney(-1);

        Set<ConstraintViolation<UserTopUpDto>> set = validator.validate(topUpDto);
        assertEquals(1, set.size());

        boolean message = false;
        for(ConstraintViolation<UserTopUpDto> violation: set){
            if(violation.getMessage().equals("top.up.invalid.input.money.value.negative")){
                message = true;
            }
        }

        assertTrue(message);
    }


    @Test
    public void checkCorrectTopUp(){
        UserTopUpDto topUpDto = new UserTopUpDto();
        topUpDto.setMoney(10);

        Set<ConstraintViolation<UserTopUpDto>> set = validator.validate(topUpDto);
        assertEquals(0, set.size());

        assertEquals(10.0, topUpDto.getMoney());
    }


    @Test
    public void checkUserCorrectRegister(){

        UserRegisterDto registerDto = UserRegisterDto.builder()
                .firstName("New")
                .lastName("User")
                .email("mail@mail.com")
                .login("MySuperLogin")
                .password("AAAABBBBBb8")
                .repeatPassword("AAAABBBBBb8")
                .birthDate("2022-12-01")
                .build();

        Set<ConstraintViolation<UserRegisterDto>> set = validator.validate(registerDto);
        assertEquals(0, set.size());


        registerDto.setEmail("");
        set = validator.validate(registerDto);
        assertEquals(1, set.size());

        boolean message = false;
        for(ConstraintViolation<UserRegisterDto> violation: set){
            if(violation.getMessage().equals("user.invalid.input.email")){
                message = true;
            }
        }

        assertTrue(message);

    }


    @Test
    public void checkUserIncorrectRegister(/*@Autowired Validator validator*/){
        UserRegisterDto registerDto = UserRegisterDto.builder()
                .firstName("New")
                .lastName("User")
                .email("")
                .login("MySuperLogin")
                .password("AAAABBBBBb8")
                .repeatPassword("AAAABBBBBb8")
                .birthDate("2022-12-01")
                .build();

        Set<ConstraintViolation<UserRegisterDto>> set = validator.validate(registerDto);
        assertEquals(1, set.size());

        boolean message = false;
        for(ConstraintViolation<UserRegisterDto> violation: set){
            if(violation.getMessage().equals("user.invalid.input.email")){
                message = true;
            }
        }
        assertTrue(message);


        //Check incorrect email
        registerDto.setEmail("mailincorrect.com");
        set = validator.validate(registerDto);
        assertEquals(1, set.size());

        message = false;
        for(ConstraintViolation<UserRegisterDto> violation: set){
            if(violation.getMessage().equals("user.invalid.input.email.pattern")){
                message = true;
            }
        }
        assertTrue(message);


        //Check passwords equality
        registerDto.setPassword(registerDto.getPassword() + "1");
        set = validator.validate(registerDto);
        assertEquals(2, set.size());

        message = false;
        for(ConstraintViolation<UserRegisterDto> violation: set){
            if(violation.getMessage().equals("user.invalid.input.passwords.equals")){
                message = true;
            }
        }
        assertTrue(message);

        //Check if password's weak
        registerDto.setPassword("djdnbfjsn2");
        set = validator.validate(registerDto);
        assertEquals(3, set.size());

        message = false;
        for(ConstraintViolation<UserRegisterDto> violation: set){
            if(violation.getMessage().equals("user.invalid.input.password.pattern")){
                message = true;
                break;
            }
        }
        assertTrue(message);


        registerDto.setPassword(registerDto.getRepeatPassword());

        //Case when login & email exists
        registerDto.setLogin("ArsenOOO7");
        registerDto.setEmail("arsen@mail.com");

        set = validator.validate(registerDto);
        for(ConstraintViolation<UserRegisterDto> violation: set){
            System.out.println(violation.getMessage());
        }
        assertEquals(2, set.size());

    }


    @Test
    public void checkPasswordChangingCorrect(){

        UserRegisterDto registerDto = UserRegisterDto.builder()
                .firstName("New")
                .lastName("User")
                .email("mail@mail.com")
                .login("MySuperLogin")
                .password("AAAABBBBBb8")
                .repeatPassword("AAAABBBBBb8")
                .birthDate("2022-12-01")
                .build();

        User user = UserRegisterDto.dtoToUser(registerDto);
        user.setRole(repository.readRoleByName("ADMIN"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        UserPasswordChangeDto passwordChangeDto = new UserPasswordChangeDto();
        passwordChangeDto.setOldPassword(registerDto.getPassword());
        passwordChangeDto.setNewPassword(registerDto.getPassword() + "2");
        passwordChangeDto.setRepeatPassword(registerDto.getPassword() + "2");

        Set<ConstraintViolation<UserPasswordChangeDto>> violations = validator.validate(passwordChangeDto);
        assertEquals(0, violations.size());

        SecurityContextHolder.clearContext();

    }


    @Test
    public void checkPasswordChangingIncorrect(){

        UserRegisterDto registerDto = UserRegisterDto.builder()
                .firstName("New")
                .lastName("User")
                .email("mail@mail.com")
                .login("MySuperLogin")
                .password("AAAABBBBBb8")
                .repeatPassword("AAAABBBBBb8")
                .birthDate("2022-12-01")
                .build();

        User user = UserRegisterDto.dtoToUser(registerDto);
        user.setRole(repository.readRoleByName("ADMIN"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        //Passwords are not equal
        //password.change.invalid.input.old
        UserPasswordChangeDto passwordChangeDto = new UserPasswordChangeDto();
        passwordChangeDto.setOldPassword(registerDto.getPassword());
        passwordChangeDto.setNewPassword(registerDto.getPassword() + "20");
        passwordChangeDto.setRepeatPassword(registerDto.getPassword() + "2");

        Set<ConstraintViolation<UserPasswordChangeDto>> violations = validator.validate(passwordChangeDto);
        assertEquals(1, violations.size());

        boolean message = false;
        for(ConstraintViolation<UserPasswordChangeDto> violation: violations){
            if(violation.getMessage().equals("user.invalid.input.passwords.equals")){
                message = true;
                break;
            }
        }
        assertTrue(message);


        passwordChangeDto.setNewPassword("AAAABBBBBb81");
        passwordChangeDto.setRepeatPassword("AAAABBBBBb81");

        //Check old password
        passwordChangeDto.setOldPassword("AAAABBB");

        violations = validator.validate(passwordChangeDto);
        assertEquals(1, violations.size());

        message = false;
        for(ConstraintViolation<UserPasswordChangeDto> violation: violations){
            if(violation.getMessage().equals("password.change.invalid.input.old.equals")){
                message = true;
                break;
            }
        }
        assertTrue(message);

        passwordChangeDto.setOldPassword("AAAABBBBBb8");


        //Check if password's weak
        passwordChangeDto.setNewPassword("djdnbfjsn2");
        violations = validator.validate(passwordChangeDto);
        assertEquals(2, violations.size());

        message = false;
        for(ConstraintViolation<UserPasswordChangeDto> violation: violations){
            if(violation.getMessage().equals("user.invalid.input.password.pattern")){
                message = true;
                break;
            }
        }
        assertTrue(message);

        SecurityContextHolder.clearContext();

    }


    @Test
    public void checkEditCorrect(){

        UserEditDto editDto = new UserEditDto();
        editDto.setFirstName("Arsen");
        editDto.setLastName("Sydoryk");
        editDto.setBirthDate("2022-12-12");

        Set<ConstraintViolation<UserEditDto>> violations = validator.validate(editDto);
        assertEquals(0, violations.size());


        assertEquals("Arsen", editDto.getFirstName());
        assertEquals("Sydoryk", editDto.getLastName());
        assertEquals("2022-12-12", editDto.getBirthDate());

    }

    @Test
    public void checkEditIncorrect(){
        UserEditDto editDto = new UserEditDto();
        editDto.setFirstName("");
        editDto.setLastName("");
        editDto.setBirthDate("2022-12-12");

        Set<ConstraintViolation<UserEditDto>> violations = validator.validate(editDto);
        assertEquals(2, violations.size());

        short counter = 0;
        for(ConstraintViolation<UserEditDto> violation: violations){
            if(violation.getMessage().equals("user.invalid.input.name")){
                ++counter;
            }

            if(violation.getMessage().equals("user.invalid.input.surname")){
                ++counter;
            }
        }

        assertEquals(2, counter);

    }

}

