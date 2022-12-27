package com.arsen.internet.shop.soap.app.model;

import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.SizeUnit;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;


/**
 * Product entity
 * @author Arsen Sydoryk
 */
@Getter
@Setter
@Entity
@Table(name = "products", indexes = @Index(name = "products_db_idx_cat_id", columnList = "category_id"))
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String shortName;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private long quantity;
    @Column(nullable = false)
    private double price;

    @Formula("(select count(cart.id) from cart where cart.product_id = id and cart.status = 'BOUGHT')")
    private long boughtCounter;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Enumerated(EnumType.STRING)
    private SizeUnit sizeUnit;

    @Column(nullable = false)
    private long sizeValue;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "preview_image", nullable = true)
    private Image image;

    @Column(nullable = false)
    private Date createdAt;

}
