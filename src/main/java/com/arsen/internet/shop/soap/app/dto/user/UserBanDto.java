package com.arsen.internet.shop.soap.app.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * User Ban DTO
 * @author Arsen Sydoryk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBanDto {

    private long id;
    private String reason;
    private long endHours;


    /**
     * Base constructor
     * @param id of user
     */
    public UserBanDto(long id){
        this.id = id;
    }

}
