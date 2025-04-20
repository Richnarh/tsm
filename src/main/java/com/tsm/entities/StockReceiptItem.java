/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.tsm.entities.system.UserAccountRecord;
import com.tsm.enums.ReceiptStatus;
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
    
    public static final String _stockReceipt = "stockReceipt";
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
    private Double pkgQuantity;
    
    @Column(name = "pkg_factor")
    private Double pkgFactor;
    
    @Column(name = "wprice")
    private Double wprice;
    
    @Column(name = "qty_sold")
    private Double qtySold;
    
    @Column(name = "qty_left")
    private Double qtyLeft;
    
    @Column(name = "cost_price")
    private Double costPrice;
    
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

    public Double getQtySold()
    {
        return qtySold;
    }

    public void setQtySold(Double qtySold)
    {
        this.qtySold = qtySold;
    }

    public Double getQtyLeft()
    {
        return qtyLeft;
    }

    public void setQtyLeft(Double qtyLeft)
    {
        this.qtyLeft = qtyLeft;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
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

    public Double getPkgQuantity() {
        return pkgQuantity;
    }

    public void setPkgQuantity(Double pkgQuantity) {
        this.pkgQuantity = pkgQuantity;
    }

    public Double getPkgFactor() {
        return pkgFactor;
    }

    public void setPkgFactor(Double pkgFactor) {
        this.pkgFactor = pkgFactor;
    }
    
    public Double getWprice() {
        return wprice;
    }

    public void setWprice(Double wprice) {
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
