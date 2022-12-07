package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Category repository
 * @author Arsen Sydoryk
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
