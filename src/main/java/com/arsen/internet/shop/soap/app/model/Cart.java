package com.arsen.internet.shop.soap.app.model;

import com.arsen.internet.shop.soap.app.model.data.CartStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;


/**
 * Cart entity
 * @author Arsen Sydoryk
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private long quantity;
    @Formula("(CASE WHEN status = 'CARTED' THEN (select (quantity * products.price) from products where products.id = product_id) else actual_price end)")
    private double price;

    @Column
    private double actualPrice;

    @Column
    private Date date;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

}
