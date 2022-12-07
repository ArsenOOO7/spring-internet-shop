package com.arsen.internet.shop.soap.app.service;

import com.arsen.internet.shop.soap.app.dto.product.ProductSearchDto;
import com.arsen.internet.shop.soap.app.exception.NullEntityReferenceException;
import com.arsen.internet.shop.soap.app.model.Product;
import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.SizeUnit;
import com.arsen.internet.shop.soap.app.repository.ProductRepository;
import com.arsen.internet.shop.soap.app.repository.build.ProductSearchRepository;
import com.arsen.internet.shop.soap.app.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductSearchRepository searchRepository;

    @Autowired
    private ProductServiceImpl productService;


    private static Product product;

    @BeforeAll
    public static void beforeAll() {
        product = new Product();

        product.setShortName("Product");
        product.setFullName("Product full name");
        product.setDescription("Some description");
        product.setQuantity(100);
        product.setPrice(100.0);
        product.setColor(Color.BLACK);
        product.setSizeUnit(SizeUnit.MM);
        product.setSizeValue(1000);
    }


    @Test
    public void createProduct(){

        assertThrows(NullEntityReferenceException.class, () -> productService.create(null));

        when(productRepository.save(any(Product.class))).thenAnswer(ans -> ans.getArgument(0));
        assertEquals(product, productService.create(product));

    }

    @Test
    public void updateProduct(){
        assertThrows(NullEntityReferenceException.class, () -> productService.update(null));

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.update(product));

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(ans -> ans.getArgument(0));
        assertEquals(product, productService.update(product));

    }


    @Test
    public void readProduct(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.readById(1));

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        assertEquals(product, productService.readById(1));
    }


    @Test
    public void deleteProduct(){
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.delete(1));

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        assertDoesNotThrow(() -> productService.delete(1));
    }

    @Test
    public void checkGetAll(){

        List<Product> expected = List.of(product, product);

        when(productRepository.findAll()).thenReturn(List.of());
        assertEquals(0, productService.getAll().size());

        when(productRepository.findAll()).thenReturn(List.of(product, product));
        assertEquals(expected, productService.getAll());

    }


    @Test
    public void checkSearchingCriteria(){
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setSearchProduct("Product");

        searchDto.setMinPrice(90);
        searchDto.setMaxPrice(1_000_00d);
        searchDto.setColor(Color.BLACK.name());

        List<Product> expected = List.of(product);

        Page<Product> page = mock(Page.class);
        when(page.getContent()).thenReturn(expected);
        when(page.getTotalPages()).thenReturn(1);

        when(searchRepository.findProductsByCriteria(searchDto)).thenReturn(page);
        Page<Product> result = productService.getAll(searchDto);

        assertEquals(expected, result.getContent());
        assertEquals(1, result.getTotalPages());


    }

}
