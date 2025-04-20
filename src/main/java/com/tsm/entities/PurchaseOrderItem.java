/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

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
 * @author richard
 */
@Entity
@Table(name = "purchase_order_item")
public class PurchaseOrderItem extends UserAccountRecord implements Serializable{
    
    @JoinColumn(name = "product", referencedColumnName = "id")
    @ManyToOne
    private Product product;
    
    public static final String _purchaseOrder = "purchaseOrder";
    @JoinColumn(name = "purchase_order", referencedColumnName = "id")
    @ManyToOne
    private PurchaseOrder purchaseOrder;
    
    @JoinColumn(name = "unit_measurement", referencedColumnName = "id")
    @ManyToOne
    private UnitMeasurement unitMeasurement;
    
    @Column(name = "qty_purchased")
    private double qtyPurchased;
    
    @Column(name = "cost_price")
    private double costPrice;
        
    @Column(name = "sub_total")
    private double subTotal;
    
    @Lob
    @Column(name = "description")
    private String description;

    
    public PurchaseOrder getPurchaseOrder()
    {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder)
    {
        this.purchaseOrder = purchaseOrder;
    }

    public double getQtyPurchased()
    {
        return qtyPurchased;
    }

    public void setQtyPurchased(double qtyPurchased)
    {
        this.qtyPurchased = qtyPurchased;
    }

    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public double getCostPrice()
    {
        return costPrice;
    }

    public void setCostPrice(double costPrice)
    {
        this.costPrice = costPrice;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public UnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(UnitMeasurement unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }
    
}
