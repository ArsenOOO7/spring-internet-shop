package com.arsen.internet.shop.soap.app.dto.user;

import com.arsen.internet.shop.soap.app.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


/**
 * User Edit DTO
 * @author Arsen Sydoryk
 */
@Setter
@Getter
@NoArgsConstructor
public class UserEditDto {

    @NotBlank(message = "user.invalid.input.name")
    private String firstName;

    @NotBlank(message = "user.invalid.input.surname")
    private String lastName;

    private String emailIgnore;

    @NotBlank(message = "user.invalid.input.birthdate")
    private String birthDate;


    /**
     * Constructor
     * @param user to edit
     */
    public UserEditDto(User user){

        firstName = user.getFirstName();
        lastName = user.getLastName();
        emailIgnore = user.getEmail();
        birthDate = user.getBirthDate().toString();

    }

}
