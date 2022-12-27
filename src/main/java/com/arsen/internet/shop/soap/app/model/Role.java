package com.arsen.internet.shop.soap.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


/**
 * Role entity
 * @author Arsen Sydoryk
 */
@Getter
@Setter
@Entity
@Table(name = "roles", indexes = @Index(name = "roles_db_idx_name", columnList = "name"))
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

}
