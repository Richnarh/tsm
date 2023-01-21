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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "inventory")
public class Inventory extends UserAccountRecord implements Serializable{
    @JoinColumn(name = "stock_receipt_item")
    @ManyToOne
    private StockReceiptItem stockReceiptItem;
    
    @JoinColumn(name = "location")
    @ManyToOne
    private Location location;
    
    @Column(name = "selling_price")
    private double sellingPrice;
    
    @Column(name = "quantity")
    private double quantity;
    
    @JoinColumn(name = "product_package", referencedColumnName = "id")
    @ManyToOne
    private ProductPackage productPackage;
    
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

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public ProductPackage getProductPackage() {
        return productPackage;
    }

    public void setProductPackage(ProductPackage productPackage) {
        this.productPackage = productPackage;
    }

    @Override
    public String toString() {
        return stockReceiptItem+"";
    }
}
