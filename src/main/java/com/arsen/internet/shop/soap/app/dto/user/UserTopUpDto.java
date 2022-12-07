package com.arsen.internet.shop.soap.app.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;


/**
 * User Top Up DTO
 * @author Arsen Sydoryk
 */
@Setter
@Getter
@NoArgsConstructor
public class UserTopUpDto {

    @Min(message = "top.up.invalid.input.money.value.negative", value = 1)
    private double money;

}
