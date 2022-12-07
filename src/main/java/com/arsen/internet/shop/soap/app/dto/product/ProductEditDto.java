package com.arsen.internet.shop.soap.app.dto.product;

import com.arsen.internet.shop.soap.app.model.Product;
import com.arsen.internet.shop.soap.app.model.data.Color;
import com.arsen.internet.shop.soap.app.model.data.SizeUnit;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * Product Edit DTO
 * @author Arsen Sydoryk
 */
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductEditDto {

    private long id;

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

    private MultipartFile preview;


    /**
     * Convert from Product
     * @param product Product
     * @return DTO
     */
    public static ProductEditDto convertToDto(Product product){
        return ProductEditDto.builder()
                .id(product.getId())
                .shortName(product.getShortName())
                .fullName(product.getFullName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .color(product.getColor())
                .sizeUnit(product.getSizeUnit())
                .sizeValue(product.getSizeValue())
                .category(product.getCategory().getId())
                .build();
    }


}
