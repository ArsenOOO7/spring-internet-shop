package com.arsen.internet.shop.soap.app.dto.product;

import com.arsen.internet.shop.soap.app.annotation.Image;
import com.arsen.internet.shop.soap.app.model.Product;
import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.SizeUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * Product DTO to create new
 * @author Arsen Sydoryk
 */
@Setter
@Getter
@NoArgsConstructor
public class ProductAddDto {

    @NotBlank(message = "product.invalid.input.shortname")
    private String shortName;
    @NotBlank(message = "product.invalid.input.fullname")
    private String fullName;
    @NotBlank(message = "product.invalid.input.description")
    private String description;

    @Min(value = 1, message = "product.invalid.input.price.value")
    private double price;

    @Min(value = 1, message = "product.invalid.input.amount.value")
    private long quantity;

    @NotNull(message = "product.invalid.input.color")
    private Color color;
    @NotNull(message = "product.invalid.input.size.unit")
    private SizeUnit sizeUnit;


    @Min(value = 1, message = "product.invalid.input.amount.value")
    private long sizeValue;
    @Min(value = 1, message = "product.invalid.input.category")
    private long category;

    @Image
    private MultipartFile preview;


    /**
     * Convert to Product entity
     * @param dto current
     * @return Product
     */
    public static Product convertToProduct(ProductAddDto dto){
        Product product = new Product();
        product.setShortName(dto.getShortName());
        product.setFullName(dto.getFullName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setColor(dto.getColor());
        product.setSizeUnit(dto.getSizeUnit());
        product.setSizeValue(dto.getSizeValue());
        return product;
    }

}
