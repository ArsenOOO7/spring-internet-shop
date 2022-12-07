package com.arsen.internet.shop.soap.app.model.data;


/**
 * Sort types
 * @author Arsen Sydoryk
 */
public enum Sort {

    NAME_ASCEND("sortNameAsc", "shortName", "ASC"),
    NAME_DESCEND("sortNameDesc", "shortName", "DESC"),
    PRICE_ASCEND("sortPriceAsc", "price", "ASC"),
    PRICE_DESCEND("sortPriceDesc", "price", "DESC"),
    NOVELTY("sortNovelty", "createdAt", "DESC"),
    UNDEFINED("", "", "");


    private final String code;
    private final String field;
    private final String order;

    Sort(String code, String field, String order){
        this.code = code;
        this.field = field;
        this.order = order;
    }

    public String getCode(){
        return code;
    }

    public String getField() {
        return field;
    }

    public String getOrder() {
        return order;
    }


    /**
     *
     * @param code of sort from query params
     * @return Sort instance
     */
    public static Sort getInstance(String code){
        Sort sort = Sort.NOVELTY;
        for(Sort value: Sort.values()){
            if(value.getCode().equalsIgnoreCase(code)){
                sort = value;
                break;
            }
        }
        return sort;
    }

}
