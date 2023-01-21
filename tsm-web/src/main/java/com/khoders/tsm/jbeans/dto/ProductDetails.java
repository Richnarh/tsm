/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.dto;

/**
 *
 * @author richa
 */
public class ProductDetails
{
    private String productName;
    private String productType;
    private String packaging;

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductType()
    {
        return productType;
    }

    public void setProductType(String productType)
    {
        this.productType = productType;
    }

    public String getPackaging()
    {
        return packaging;
    }

    public void setPackaging(String packaging)
    {
        this.packaging = packaging;
    }
    
}
