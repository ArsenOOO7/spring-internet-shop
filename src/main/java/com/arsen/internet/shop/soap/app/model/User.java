package com.arsen.internet.shop.soap.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.List;


/**
 * User entity
 * @author Arsen Sydoryk
 */
@Table(name = "users")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String firstName;
    @Column
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String login;
    @Column
    private String password;

    @Column
    private Date birthDate;

    @Column
    private double balance = 0.0;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "image_id", columnDefinition = "bigint default 0", nullable = true)
    private Image image;


    @OneToOne(mappedBy = "user")
    private UserBlock block;


    /**
     * Constructor
     * @param firstName
     * @param lastName
     * @param email
     * @param login
     * @param password
     * @param date
     */
    public User(String firstName, String lastName, String email, String login, String password, Date date){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.password = password;
        birthDate = date;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
