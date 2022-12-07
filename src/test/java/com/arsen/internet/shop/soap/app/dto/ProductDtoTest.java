package com.arsen.internet.shop.soap.app.dto;

import com.arsen.internet.shop.soap.app.InternetShopApplication;
import com.arsen.internet.shop.soap.app.dto.product.ProductAddDto;
import com.arsen.internet.shop.soap.app.dto.product.ProductEditDto;
import com.arsen.internet.shop.soap.app.dto.product.ProductSearchDto;
import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.SizeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InternetShopApplication.class)
@ActiveProfiles("test")
public class ProductDtoTest {

    @Autowired
    private Validator validator;

    @Test
    public void checkSearch(){

        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setSearchProduct("Television");
        searchDto.setMinPrice(1_000d);
        searchDto.setMaxPrice(1_000_00d);
        searchDto.setColor(Color.GOLD.name());
        searchDto.setCategory(1);
        searchDto.setMinSize(10);
        searchDto.setMaxSize(1_000);

        String query = searchDto.buildQuery();
        String[] parts = query.split("&");

        assertEquals(7, parts.length);

    }


    @Test
    public void checkProductAddCorrect(){

        ProductAddDto productAddDto = new ProductAddDto();
        productAddDto.setShortName("Test");
        productAddDto.setFullName("Fullname");
        productAddDto.setDescription("Description");
        productAddDto.setPrice(10d);
        productAddDto.setQuantity(100);
        productAddDto.setColor(Color.BLACK);
        productAddDto.setSizeUnit(SizeUnit.CM);
        productAddDto.setSizeValue(100);
        productAddDto.setCategory(1);
        productAddDto.setPreview(new MultipartFile() {
            @Override
            public String getName() {
                return "Just-for-test";
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 1;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        });

        assertEquals(0, validator.validate(productAddDto).size());

    }

    @Test
    public void checkProductAddInCorrect(){

        ProductAddDto productAddDto = new ProductAddDto();
        productAddDto.setShortName("Test");
        productAddDto.setFullName("");
        productAddDto.setDescription("Description");
        productAddDto.setPrice(10d);
        productAddDto.setQuantity(100);
        productAddDto.setColor(Color.BLACK);
        productAddDto.setSizeUnit(SizeUnit.CM);
        productAddDto.setSizeValue(100);
        productAddDto.setCategory(1);
        productAddDto.setPreview(null);

        assertEquals(2, validator.validate(productAddDto).size());

    }
    
    
    @Test
    public void checkProductEditCorrect(){
        ProductEditDto productEditDto = new ProductEditDto();
        productEditDto.setShortName("Test");
        productEditDto.setFullName("Fullname");
        productEditDto.setDescription("Description");
        productEditDto.setPrice(10d);
        productEditDto.setQuantity(100);
        productEditDto.setColor(Color.BLACK);
        productEditDto.setSizeUnit(SizeUnit.CM);
        productEditDto.setSizeValue(100);
        productEditDto.setCategory(1);

        assertEquals(0, validator.validate(productEditDto).size());
    }


    @Test
    public void checkProductEditIncorrect(){
        ProductEditDto productEditDto = new ProductEditDto();
        productEditDto.setShortName("Test");
        productEditDto.setFullName("");
        productEditDto.setDescription("");
        productEditDto.setPrice(10d);
        productEditDto.setQuantity(100);
        productEditDto.setColor(Color.BLACK);
        productEditDto.setSizeUnit(SizeUnit.CM);
        productEditDto.setSizeValue(100);
        productEditDto.setCategory(1);

        assertEquals(2, validator.validate(productEditDto).size());
    }

}
