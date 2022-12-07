package com.arsen.internet.shop.soap.app.service.impl;

import com.arsen.internet.shop.soap.app.dto.product.ProductSearchDto;
import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Product;
import com.arsen.internet.shop.soap.app.repository.ProductRepository;
import com.arsen.internet.shop.soap.app.repository.build.ProductSearchRepository;
import com.arsen.internet.shop.soap.app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class    ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductSearchRepository productSearchRepository) {
        this.productRepository = productRepository;
        this.productSearchRepository = productSearchRepository;
    }

    @Override
    public Product create(Product product) {
        if(product == null){
            throw new NullEntityReferenceException("Product cannot be null!");
        }

        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        if(product == null){
            throw new NullEntityReferenceException("Product cannot be null!");
        }

        readById(product.getId());
        return productRepository.save(product);
    }

    @Override
    public Product readById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + id + " is not found!"));
    }

    @Override
    public void delete(long id) {
        productRepository.delete(readById(id));
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAll(ProductSearchDto searchDto) {
        return productSearchRepository.findProductsByCriteria(searchDto);
    }
}
