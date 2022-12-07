package com.arsen.internet.shop.soap.app.dto.product;

import lombok.Data;

import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * DTO for searching data in DB
 * @author Arsen Sydoryk
 */
@Data
public class ProductSearchDto {

    private int page = 1;
    private String searchProduct = "";

    private double minPrice;
    private double maxPrice;

    private long category;
    private String color = "";

    private long minSize;
    private long maxSize;

    private String sort = "";


    /**
     * Builds query for GET-request
     * @return String
     */
    public String buildQuery(){

        Map<String, String> map = new Hashtable<>();

        if(!searchProduct.isEmpty()){
            map.put("searchProduct", searchProduct);
        }

        if(minPrice > 0){
            map.put("minPrice", "" + minPrice);
        }

        if(maxPrice > 0){
            map.put("maxPrice", "" + maxPrice);
        }


        if(category > 0){
            map.put("category", "" + category);
        }

        if(!color.isEmpty()){
            map.put("color", "" + color);
        }

        if(minSize > 0){
            map.put("minSize", "" + minSize);
        }

        if(maxSize > 0){
            map.put("maxSize", "" + maxSize);
        }

        if(!sort.isEmpty()){
            map.put("sort", "" + sort);
        }

        return map.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

}
