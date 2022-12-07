package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Product repository
 * @author Arsen Sydoryk
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
