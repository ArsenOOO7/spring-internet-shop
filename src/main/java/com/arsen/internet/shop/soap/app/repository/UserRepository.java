package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * User repository
 * @author Arsen Sydoryk
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * Find user by email
     * @param email String
     * @return User
     */
    User findByEmail(String email);

    /**
     * Find user by login
     * @param login String
     * @return User
     */
    User findByLogin(String login);

    /**
     * Check if there is user with this email
     * @param email String
     * @return boolean
     */
    boolean existsByEmail(String email);

    /**
     * Check if there is user with this login
     * @param login String
     * @return boolean
     */
    boolean existsByLogin(String login);
}
