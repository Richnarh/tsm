/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.dto;

/**
 *
 * @author Pascal
 */
public class StockDetails {
    private String productName;
    private String productType;
    private int reorderLevel;
    private String location;
    private String packaging;
    private String unitsMeasurement;
    private double wprice;
    private double retailPrice;
    private double qtyInShop;
    private double qtyInWarehouse;
    private double costPrice;
    private double unitsInPackage;

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getUnitsMeasurement() {
        return unitsMeasurement;
    }

    public void setUnitsMeasurement(String unitsMeasurement) {
        this.unitsMeasurement = unitsMeasurement;
    }

    public double getUnitsInPackage() {
        return unitsInPackage;
    }

    public void setUnitsInPackage(double unitsInPackage) {
        this.unitsInPackage = unitsInPackage;
    }

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

    public double getWprice() {
        return wprice;
    }

    public void setWprice(double wprice) {
        this.wprice = wprice;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
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
