/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "inventory") // Shop
public class Inventory extends UserAccountRecord implements Serializable{
    
    public static final String _stockReceiptItem = "stockReceiptItem";
    @JoinColumn(name = "stock_receipt_item")
    @ManyToOne
    private StockReceiptItem stockReceiptItem;
    
    public static final String _location = "location";
    @JoinColumn(name = "location") // shop name
    @ManyToOne
    private Location location;
    
    @Column(name = "units_in_package")
    private double unitsInPackage;
    
    public static final String _packagePrice = "packagePrice";
    @Column(name = "package_price")
    private double packagePrice; //selling price, retail price
    
    public static final String _wprice = "wprice";
    @Column(name = "wprice")
    private double wprice; //whole sale price
    
    @Column(name = "description")
    @Lob
    private String description;
    
    @Column(name = "qty_in_shop")
    private double qtyInShop;
    
    public static final String _unitMeasurement = "unitMeasurement";
    @JoinColumn(name = "units_measurement", referencedColumnName = "id")
    @ManyToOne
    private UnitMeasurement unitMeasurement;
    
    public StockReceiptItem getStockReceiptItem() {
        return stockReceiptItem;
    }

    public void setStockReceiptItem(StockReceiptItem stockReceiptItem) {
        this.stockReceiptItem = stockReceiptItem;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getUnitsInPackage() {
        return unitsInPackage;
    }

    public void setUnitsInPackage(double unitsInPackage) {
        this.unitsInPackage = unitsInPackage;
    }

    public double getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(double packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(UnitMeasurement unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public double getQtyInShop() {
        return qtyInShop;
    }

    public void setQtyInShop(double qtyInShop) {
        this.qtyInShop = qtyInShop;
    }

    public double getWprice() {
        return wprice;
    }

    public void setWprice(double wprice) {
        this.wprice = wprice;
    }
    
    @Override
    public String toString() {
        if(unitMeasurement != null && unitsInPackage != 0.0)
           return stockReceiptItem+" ("+unitMeasurement.getUnits()+" - "+(int)unitsInPackage+")";
        else if(unitMeasurement == null && unitsInPackage != 0)
            return stockReceiptItem+" ("+(int)unitsInPackage+")";
        else if(unitMeasurement != null && unitsInPackage == 0.0)
            return stockReceiptItem+" ("+unitMeasurement.getUnits()+")";
        else
            return stockReceiptItem+"";
    }
}
