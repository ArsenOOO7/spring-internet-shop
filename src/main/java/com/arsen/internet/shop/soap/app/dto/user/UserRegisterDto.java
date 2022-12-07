package com.arsen.internet.shop.soap.app.dto.user;

import com.arsen.internet.shop.soap.app.annotation.FieldsMatch;
import com.arsen.internet.shop.soap.app.annotation.Password;
import com.arsen.internet.shop.soap.app.annotation.UniqueEmail;
import com.arsen.internet.shop.soap.app.annotation.UniqueLogin;
import com.arsen.internet.shop.soap.app.model.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.sql.Date;


/**
 * Register User DTO
 * @author Arsen Sydoryk
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldsMatch.List({
        @FieldsMatch(
                field = "password",
                fieldMatch = "repeatPassword",
                message = "user.invalid.input.passwords.equals"
        )
})
public class UserRegisterDto {

    @NotBlank(message = "user.invalid.input.name")
    private String firstName;

    @NotBlank(message = "user.invalid.input.surname")
    private String lastName;

    @NotBlank(message = "user.invalid.input.email")
    @Email(message = "user.invalid.input.email.pattern")
    @UniqueEmail
    private String email;

    @Setter @Getter
    @NotBlank(message = "user.invalid.input.login")
    @UniqueLogin
    private String login;

    @Password
    private String password;
    @Password
    private String repeatPassword;

    @NotBlank(message = "user.invalid.input.birthdate")
    private String birthDate;

    private MultipartFile avatar;


    /**
     * Convert DTO to user
     * @param dto current DTO
     * @return User
     */
    public static User dtoToUser(UserRegisterDto dto){
        return new User(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getLogin(),
                dto.getPassword(),
                Date.valueOf(dto.getBirthDate())
        );
    }




}
