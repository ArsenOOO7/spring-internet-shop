package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Category;
import org.springframework.data.domain.Page;

import javax.persistence.EntityNotFoundException;
import java.util.List;
/**
 * Category Service
 * @author Arsen Sydoryk
 */
public interface CategoryService {

    /**
     * Create new category
     * @param category category
     * @return created category
     * @throws NullEntityReferenceException if category is null
     */
    Category create(Category category);


    /**
     * Update category
     * @param category category
     * @return updated category
     * @throws EntityNotFoundException if there is no category with such id
     * @throws NullEntityReferenceException if category is null
     */
    Category update(Category category);

    /**
     * Find category
     * @param id of category
     * @return category
     * @throws EntityNotFoundException if there is no category with such id
     */
    Category readById(long id);


    /**
     * Delete category
     * @param id of category
     * @throws EntityNotFoundException if there is no category with such id
     */
    void delete(long id);


    /**
     * Get categories by page
     * @param page number
     * @return Page with Categories
     */
    Page<Category> getAll(int page);


    /**
     * Get all Categories
     * @return List of Categories
     */
    List<Category> getAll();

}
