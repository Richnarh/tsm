/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.resource.enums.DeliveryStatus;
import com.khoders.tsm.entities.system.UserAccountRecord;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author khoders
 */
@Entity
@Table(name = "sale_item")
public class SaleItem extends UserAccountRecord implements Serializable{
    
    @Column(name = "quantity")
    private double quantity=1;

    @Column(name = "unit_price")
    private double unitPrice;
    
    @Column(name = "sub_total")
    private double subTotal;
    
    public static final String _customer = "customer";
    @JoinColumn(name = "customer")
    @ManyToOne
    private Customer customer;
    
    public static final String _sales = "sales";
    @JoinColumn(name = "sales", referencedColumnName = "id")
    @ManyToOne
    private Sales sales;
    
    public static final String _inventory = "inventory";
    @JoinColumn(name = "inventory")
    @ManyToOne
    private Inventory inventory;
    
    public static final String _deliveryStatus = "deliveryStatus";
    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "whole_sale")
    private boolean wholeSale;
    
    public double getQuantity()
    {
        return quantity;
    }

    public void setQuantity(double quantity)
    {
        this.quantity = quantity;
    }
    
    public double getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice)
    {
        this.unitPrice = unitPrice;
    }
    
    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getSubTotal()
    {
        return subTotal;
    }

    public void setSubTotal(double subTotal)
    {
        this.subTotal = subTotal;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    
    public Sales getSales()
    {
        return sales;
    }

    public void setSales(Sales sales)
    {
        this.sales = sales;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public boolean isWholeSale() {
        return wholeSale;
    }

    public void setWholeSale(boolean wholeSale) {
        this.wholeSale = wholeSale;
    }
    
    @Override
    public String toString() {
        return inventory+"";
    }
    
}
