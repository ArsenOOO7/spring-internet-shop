package com.arsen.internet.shop.soap.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * Category entity
 * @author Arsen Sydoruk
 */
@Entity
@Table(name = "categories")
@Setter
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "category.invalid.input.identifier")
    @Column
    private String identifier;

    @NotBlank(message = "category.invalid.input.locale.ua")
    @Pattern(regexp = "^[А-Яа-яІіЄєҐґЇї\\s\\d]+$", message = "category.invalid.input.locale.ua.value")
    @Column
    private String localeUa;

    @NotBlank(message = "category.invalid.input.locale.en")
    @Pattern(regexp = "^[A-Za-z\\s\\d]+$", message = "category.invalid.input.locale.en.value")
    @Column
    private String localeEn;

}