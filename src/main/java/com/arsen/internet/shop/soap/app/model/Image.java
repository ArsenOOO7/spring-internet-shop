package com.arsen.internet.shop.soap.app.model;

import lombok.*;

import javax.persistence.*;


/**
 * Image entity
 * @author Arsen Sydoryk
 */
@Entity
@Table(name = "images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String uuid;

    @Column
    private String contentType;

    @Column(name = "data", unique = false, nullable = false, length = 1_000_000)
    private byte[] data;

}
