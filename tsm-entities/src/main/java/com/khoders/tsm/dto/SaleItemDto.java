/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.dto;

/**
 *
 * @author richa
 */
public class SaleItemDto
{
    private double unitPrice;
    private double quantity;
    private double subTotal;
    private String product;
    private String productPackage;

    public double getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public double getQuantity()
    {
        return quantity;
    }

    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }

    public double getSubTotal()
    {
        return subTotal;
    }

    public void setSubTotal(double subTotal)
    {
        this.subTotal = subTotal;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getProductPackage()
    {
        return productPackage;
    }

    public void setProductPackage(String productPackage)
    {
        this.productPackage = productPackage;
    }
    
}
