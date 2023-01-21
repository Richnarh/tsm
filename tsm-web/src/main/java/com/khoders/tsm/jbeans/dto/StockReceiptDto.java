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
public class StockReceiptDto
{
    private String id;
    private String refNo;
    private String productName;
    private double pkgQuantity;
    private String productPackage;
    private double packageFactor;
    private double costPrice;
    private double packagePrice;
    private int reorderLevel;

    public String getRefNo()
    {
        return refNo;
    }

    public void setRefNo(String refNo)
    {
        this.refNo = refNo;
    }

    public String getProductPackage()
    {
        return productPackage;
    }

    public void setProductPackage(String productPackage)
    {
        this.productPackage = productPackage;
    }

    public double getPkgQuantity()
    {
        return pkgQuantity;
    }

    public void setPkgQuantity(double pkgQuantity)
    {
        this.pkgQuantity = pkgQuantity;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public double getPackageFactor()
    {
        return packageFactor;
    }

    public void setPackageFactor(double packageFactor)
    {
        this.packageFactor = packageFactor;
    }

    public double getCostPrice()
    {
        return costPrice;
    }

    public void setCostPrice(double costPrice)
    {
        this.costPrice = costPrice;
    }

    public double getPackagePrice()
    {
        return packagePrice;
    }

    public void setPackagePrice(double packagePrice)
    {
        this.packagePrice = packagePrice;
    }

    public int getReorderLevel()
    {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel)
    {
        this.reorderLevel = reorderLevel;
    }
    
}
