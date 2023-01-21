/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.dto;

/**
 *
 * @author Pascal
 */
public class StockDetails {
    private String productName;
    private String productType;
    private int reorderLevel;
    private String location;
    private double sellingPrice;
    private double qtyInShop;
    private double qtyInWarehouse;
    private double costPrice;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getQtyInShop() {
        return qtyInShop;
    }

    public void setQtyInShop(double qtyInShop) {
        this.qtyInShop = qtyInShop;
    }

    public double getQtyInWarehouse() {
        return qtyInWarehouse;
    }

    public void setQtyInWarehouse(double qtyInWarehouse) {
        this.qtyInWarehouse = qtyInWarehouse;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }
    
}
