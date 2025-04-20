/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.dolphindoors.resource.jpa.BaseModel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "price_package")
public class PricePackage extends BaseModel{
    
    public static final String _sellingPrice = "sellingPrice";
    @Column(name = "selling_price")
    private Double sellingPrice;
    
    public static final String _inventory = "inventory";
    @JoinColumn(name = "inventory")
    @ManyToOne
    private Inventory inventory;
    
    public static final String _packaging = "packaging";
    @JoinColumn(name = "packaging")
    @ManyToOne
    private Packaging packaging;

    public Packaging getPackaging() {
        return packaging;
    }

    public void setPackaging(Packaging packaging) {
        this.packaging = packaging;
    }
    
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    @Override
    public String toString() {
        return inventory +" - "+ packaging;
    }
}
