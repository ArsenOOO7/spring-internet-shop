package com.arsen.internet.shop.soap.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


/**
 * User Ban entity
 * @author Arsen Sydoryk
 */
@Entity
@Table(name = "users_block", indexes = @Index(name = "users_blocks_db_idx_user", columnList = "user_id"))
@Setter
@Getter
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private long startTime;

    @Column(nullable = false)
    private long endTime;

    /**
     * @return if user's banned
     */
    public boolean isBanned(){
        long currentTime = System.currentTimeMillis() / 1_000;
        return startTime <= currentTime && currentTime <= endTime;
    }

}
