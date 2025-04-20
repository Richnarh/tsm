/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.dto;

/**
 *
 * @author richa
 */
public class StockSummary extends BaseDto
{
    private String id;
    private String refNo;
    private String productName;
    private double pkgQuantity;
    private double qtySold;
    private double costPrice;
    private int reorderLevel;

    public String getRefNo()
    {
        return refNo;
    }

    public void setRefNo(String refNo)
    {
        this.refNo = refNo;
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

    public double getCostPrice()
    {
        return costPrice;
    }

    public void setCostPrice(double costPrice)
    {
        this.costPrice = costPrice;
    }

    public int getReorderLevel()
    {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel)
    {
        this.reorderLevel = reorderLevel;
    }

    public double getQtySold()
    {
        return qtySold;
    }

    public void setQtySold(double qtySold)
    {
        this.qtySold = qtySold;
    }
}
