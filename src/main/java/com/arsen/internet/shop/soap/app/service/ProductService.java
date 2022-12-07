package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.dto.product.ProductSearchDto;
import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Product;
import org.springframework.data.domain.Page;

import javax.persistence.EntityNotFoundException;
import java.util.List;


/**
 * Product Service
 * @author Arsen Sydoryk
 */
public interface ProductService {


    /**
     * Create product
     * @param product to create
     * @return created product
     * @throws NullEntityReferenceException if product is null
     */
    Product create(Product product);

    /**
     * Update product
     * @param product to update
     * @return updated product
     * @throws NullEntityReferenceException if product is null
     * @throws EntityNotFoundException if there is no product with such id
     */
    Product update(Product product);


    /**
     * Find product
     * @param id of product
     * @return product
     * @throws EntityNotFoundException if there is no product with such id
     */
    Product readById(long id);


    /**
     * Delete product
     * @param id of product
     * @throws EntityNotFoundException if there is no product with such id
     */
    void delete(long id);


    /**
     * Get all products
     * @return List with products
     */
    List<Product> getAll();


    /**
     * Get products
     * @param searchDto with params to search
     * @return Page with Products
     */
    Page<Product> getAll(ProductSearchDto searchDto);


}
