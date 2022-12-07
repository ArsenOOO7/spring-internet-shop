package com.arsen.internet.shop.soap.app.dto.user;

import com.arsen.internet.shop.soap.app.annotation.FieldsMatch;
import com.arsen.internet.shop.soap.app.annotation.OldPassword;
import com.arsen.internet.shop.soap.app.annotation.Password;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


/**
 * Change Password DTO
 * @author Arsen Sydoryk
 */
@Setter
@Getter
@NoArgsConstructor
@FieldsMatch(field = "newPassword", fieldMatch = "repeatPassword", message = "user.invalid.input.passwords.equals")
public class UserPasswordChangeDto {

    @NotBlank(message = "password.change.invalid.input.old")
    @OldPassword
    private String oldPassword;

    @Password
    private String newPassword;
    @Password
    private String repeatPassword;

}
