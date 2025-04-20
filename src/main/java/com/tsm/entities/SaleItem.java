/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.dolphindoors.resource.enums.DeliveryStatus;
import com.tsm.entities.system.UserAccountRecord;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author khoders
 */
@Entity
@Table(name = "sale_item")
public class SaleItem extends UserAccountRecord implements Serializable{
    
    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "unit_price")
    private Double unitPrice;
    
    @Column(name = "sub_total")
    private Double subTotal;
    
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
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

    @Override
    public String toString() {
        return inventory+"";
    }
    
}
