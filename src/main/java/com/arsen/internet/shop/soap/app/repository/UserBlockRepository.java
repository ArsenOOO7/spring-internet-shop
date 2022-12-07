package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.UserBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * User Ban Repository
 * @author Arsen Sydoryk
 */
@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {


    /**
     * Find by user id
     * @param userId of user
     * @return UserBlock
     */
    UserBlock findUserBlockByUserId(long userId);


    /**
     * Find the whole banned users
     * @return List of UserBlock
     */
    @Query(nativeQuery = true, value = "select * from users_block where (select round(extract(epoch from now()))) between start_time and end_time")
    List<UserBlock> readAll();


    /**
     * Find banned users with pagination
     * @param pageable Page
     * @return Page with blocks
     */
    @Query(nativeQuery = true, value = "select * from users_block where (select round(extract(epoch from now()))) between start_time and end_time \n--#pageable\n")
    Page<UserBlock> findAllBy(Pageable pageable);

}
