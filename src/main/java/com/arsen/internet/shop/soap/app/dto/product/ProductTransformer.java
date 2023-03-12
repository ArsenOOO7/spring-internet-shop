package com.arsen.internet.shop.soap.app.dto.product;

import com.arsen.internet.shop.soap.app.model.Product;

public class ProductTransformer {

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
