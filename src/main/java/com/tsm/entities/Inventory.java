/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.dolphindoors.resource.utilities.JUtils;
import com.tsm.entities.system.UserAccountRecord;
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
    
    public static final String _product = "product";
    @JoinColumn(name = "product")
    @ManyToOne
    private Product product;
    
    @Column(name = "inventory_code", unique = true)
    private String inventoryCode = JUtils.generate(6).toUpperCase();
    
    @Column(name = "description")
    @Lob
    private String description;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "quantity_sold")
    private Integer quantitySold;
    
//    public static final String _defaultPackage = "defaultPackage";
//    @JoinColumn(name = "default_package")
//    @ManyToOne
//    private Packaging defaultPackage;
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    @Override
    public String toString() {
        return product+"";
    }
}
