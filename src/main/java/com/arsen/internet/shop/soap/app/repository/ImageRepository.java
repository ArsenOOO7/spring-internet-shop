package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Image repository
 * @author Arsen Sydoryk
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
