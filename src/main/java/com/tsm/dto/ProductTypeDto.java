package com.tsm.dto;

/**
 *
 * @author richardnarh
 */
public class ProductTypeDto extends Base{
    private String productTypeName;

    public ProductTypeDto() {
    }

    public ProductTypeDto(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }
    
}
