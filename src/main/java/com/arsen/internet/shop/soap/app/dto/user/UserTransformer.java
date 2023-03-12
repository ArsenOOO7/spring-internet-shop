package com.arsen.internet.shop.soap.app.dto.user;

import com.arsen.internet.shop.soap.app.model.User;

import java.sql.Date;

public class UserTransformer {


    /**
     * Convert DTO to user
     * @param userRegisterDto current DTO
     * @return User
     */
    public static User registerDtoToUser(UserRegisterDto userRegisterDto){
        return new User(
                userRegisterDto.getFirstName(),
                userRegisterDto.getLastName(),
                userRegisterDto.getEmail(),
                userRegisterDto.getLogin(),
                userRegisterDto.getPassword(),
                Date.valueOf(userRegisterDto.getBirthDate())
        );
    }

}
