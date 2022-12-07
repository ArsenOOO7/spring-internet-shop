package com.arsen.internet.shop.soap.app.service.impl;

import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Category;
import com.arsen.internet.shop.soap.app.repository.CategoryRepository;
import com.arsen.internet.shop.soap.app.service.CategoryService;
import com.arsen.internet.shop.soap.app.utils.ShopValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) {
        if(category == null){
            throw new NullEntityReferenceException("Category cannot be null!");
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        if(category == null){
            throw new NullEntityReferenceException("Category cannot be null!");
        }

        readById(category.getId());
        return categoryRepository.save(category);
    }

    @Override
    public Category readById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found!"));
    }

    @Override
    public void delete(long id) {
        categoryRepository.delete(readById(id));
    }

    @Override
    public Page<Category> getAll(int page) {
        return categoryRepository.findAll(PageRequest.of(--page, ShopValues.MAX_ROWS));
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}
