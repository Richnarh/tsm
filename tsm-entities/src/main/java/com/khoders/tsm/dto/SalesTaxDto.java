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
public class SalesTaxDto
{
    private String salesTaxId;
    private String taxName;
    private double taxRate;
    private double taxAmount;
    private int reOrder;

    public String getSalesTaxId()
    {
        return salesTaxId;
    }

    public void setSalesTaxId(String salesTaxId)
    {
        this.salesTaxId = salesTaxId;
    }

    public String getTaxName()
    {
        return taxName;
    }

    public void setTaxName(String taxName)
    {
        this.taxName = taxName;
    }

    public double getTaxRate()
    {
        return taxRate;
    }

    public void setTaxRate(double taxRate)
    {
        this.taxRate = taxRate;
    }

    public double getTaxAmount()
    {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    public int getReOrder()
    {
        return reOrder;
    }

    public void setReOrder(int reOrder)
    {
        this.reOrder = reOrder;
    }
    
}
