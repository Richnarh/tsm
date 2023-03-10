/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
import com.khoders.tsm.enums.ReceiptStatus;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author richard
 */
@Entity
@Table(name = "stock_receipt_item")
public class StockReceiptItem extends UserAccountRecord implements Serializable{
    
    @JoinColumn(name = "stock_receipt", referencedColumnName = "id")
    @ManyToOne
    private StockReceipt stockReceipt;
    
    @JoinColumn(name = "product", referencedColumnName = "id")
    @ManyToOne
    private Product product;
    
    @JoinColumn(name = "purchase_order_item", referencedColumnName = "id")
    @ManyToOne
    private PurchaseOrderItem purchaseOrderItem;
    
    @JoinColumn(name = "unit_measurement", referencedColumnName = "id")
    @ManyToOne
    private UnitMeasurement unitMeasurement;
    
    @Column(name = "receipt_status")
    @Enumerated(EnumType.STRING)
    private ReceiptStatus receiptStatus = ReceiptStatus.PENDING;
    
    @Column(name = "pkg_quantity")
    private double pkgQuantity;
    
    @Column(name = "pkg_factor")
    private double pkgFactor;
    
    @Column(name = "wprice")
    private double wprice;
    
    @Column(name = "qty_sold")
    private double qtySold;
    
    @Column(name = "qty_left")
    private double qtyLeft;
    
    @Column(name = "cost_price")
    private double costPrice;
    
    @Column(name = "description")
    @Lob
    private String description;
    
    public StockReceipt getStockReceipt() {
        return stockReceipt;
    }

    public void setStockReceipt(StockReceipt stockReceipt) {
        this.stockReceipt = stockReceipt;
    }

    public PurchaseOrderItem getPurchaseOrderItem() {
        return purchaseOrderItem;
    }

    public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        this.purchaseOrderItem = purchaseOrderItem;
    }

    public double getQtySold()
    {
        return qtySold;
    }

    public void setQtySold(double qtySold)
    {
        this.qtySold = qtySold;
    }

    public double getQtyLeft()
    {
        return qtyLeft;
    }

    public void setQtyLeft(double qtyLeft)
    {
        this.qtyLeft = qtyLeft;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

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

    public double getPkgQuantity() {
        return pkgQuantity;
    }

    public void setPkgQuantity(double pkgQuantity) {
        this.pkgQuantity = pkgQuantity;
    }

    public double getPkgFactor() {
        return pkgFactor;
    }

    public void setPkgFactor(double pkgFactor) {
        this.pkgFactor = pkgFactor;
    }
    
    public double getWprice() {
        return wprice;
    }

    public void setWprice(double wprice) {
        this.wprice = wprice;
    }

    public UnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(UnitMeasurement unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public ReceiptStatus getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(ReceiptStatus receiptStatus) {
        this.receiptStatus = receiptStatus;
    }
    
    @Override
    public String toString()
    {
        return product+"";
    }
    
    
}
