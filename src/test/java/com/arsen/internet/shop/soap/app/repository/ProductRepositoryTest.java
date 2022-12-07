package com.arsen.internet.shop.soap.app.repository;

import com.arsen.internet.shop.soap.app.dto.product.ProductSearchDto;
import com.arsen.internet.shop.soap.app.model.Category;
import com.arsen.internet.shop.soap.app.model.Product;
import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.SizeUnit;
import com.arsen.internet.shop.soap.app.model.data.Sort;
import com.arsen.internet.shop.soap.app.repository.build.ProductSearchRepository;
import com.arsen.internet.shop.soap.app.utils.ShopValues;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Order(value = 1)
    public void createProductTest(){

        Category category = new Category();
        category.setIdentifier("test");
        category.setLocaleEn("Test");
        category.setLocaleUa("Тест");

        Product product = new Product();

        product.setShortName("Product");
        product.setFullName("Product full name");
        product.setDescription("Some description");
        product.setQuantity(100);
        product.setPrice(100.0);
        product.setColor(Color.BLACK);
        product.setSizeUnit(SizeUnit.MM);
        product.setSizeValue(1000);

        product.setCategory(categoryRepository.save(category));

        Date date = Date.from(Instant.now());
        product.setCreatedAt(date);

        product = productRepository.save(product);

        assertEquals(1, product.getId());
    }


    @Test
    @Order(value = 2)
    public void checkReadingProduct(){

        Category category = new Category();
        category.setIdentifier("test");
        category.setLocaleEn("Test");
        category.setLocaleUa("Тест");

        Product product = new Product();

        product.setShortName("Product");
        product.setFullName("Product full name");
        product.setDescription("Some description");
        product.setQuantity(100);
        product.setPrice(100.0);
        product.setColor(Color.BLACK);
        product.setSizeUnit(SizeUnit.MM);
        product.setSizeValue(1000);

        product.setCategory(categoryRepository.save(category));

        Date date = Date.from(Instant.now());
        product.setCreatedAt(date);

        product = productRepository.save(product);
        Product readProduct = productRepository.findById(product.getId()).orElse(null);

        assertNotNull(readProduct);
        assertEquals(product.getId(), readProduct.getId());

    }


    @Test
    @Order(value = 3)
    public void delete(){

        Category category = new Category();
        category.setIdentifier("test");
        category.setLocaleEn("Test");
        category.setLocaleUa("Тест");

        Product product = new Product();

        product.setShortName("Product");
        product.setFullName("Product full name");
        product.setDescription("Some description");
        product.setQuantity(100);
        product.setPrice(100.0);
        product.setColor(Color.BLACK);
        product.setSizeUnit(SizeUnit.MM);
        product.setSizeValue(1000);

        product.setCategory(categoryRepository.save(category));

        Date date = Date.from(Instant.now());
        product.setCreatedAt(date);

        product = productRepository.save(product);
        productRepository.delete(product);

        assertNull(productRepository.findById(product.getId()).orElse(null));

    }


    @Test
    @Order(value = 4)
    public void readByPage(){

        Category category = new Category();
        category.setIdentifier("test");
        category.setLocaleEn("Test");
        category.setLocaleUa("Тест");

        category = categoryRepository.save(category);


        productRepository.saveAll(getProducts(category));

        Page<Product> page = productRepository.findAll(PageRequest.of(0, ShopValues.MAX_ROWS));
        assertEquals(ShopValues.MAX_ROWS, page.getContent().size());

    }

    @Test
    @Order(value = 5)
    public void readByCriteria(){

        Category category = new Category();
        category.setIdentifier("test");
        category.setLocaleEn("Test");
        category.setLocaleUa("Тест");

        category = categoryRepository.save(category);


        productRepository.saveAll(getProducts(category));

        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setSearchProduct("Test");

        Page<Product> result = productSearchRepository.findProductsByCriteria(searchDto);
        assertEquals(ShopValues.MAX_ROWS, result.getContent().size());

        searchDto.setMinPrice(100);
        searchDto.setMaxPrice(102);

        result = productSearchRepository.findProductsByCriteria(searchDto);
        assertEquals(3, result.getContent().size());

        searchDto.setMinPrice(0);
        searchDto.setMaxPrice(0);
        searchDto.setSort(Sort.NAME_ASCEND.getCode());

        result = productSearchRepository.findProductsByCriteria(searchDto);

        List<Product> list = result.getContent();
        assertFalse(list.isEmpty());

        int compare = -1;
        if(list.size() >= 2){
            compare = list.get(0).getShortName().compareTo(list.get(1).getShortName());
        }

        assertEquals(-1, compare);

    }


    private static final int counter = 10;
    private static List<Product> getProducts(Category category){
        List<Product> products = new LinkedList<>();
        Product product;
        for(int i = 0; i < counter; ++i){
            product = new Product();
            product.setShortName("Test " + i);
            product.setFullName("Full name test " + i);
            product.setDescription("Description " + i);
            product.setQuantity(100 + i);
            product.setPrice(100.0 + i);
            product.setCategory(category);
            product.setColor(Color.BLACK);
            product.setSizeUnit(SizeUnit.CM);
            product.setSizeValue(100);
            product.setCreatedAt(Date.from(Instant.now()));
            products.add(product);
        }

        return products;
    }

}

