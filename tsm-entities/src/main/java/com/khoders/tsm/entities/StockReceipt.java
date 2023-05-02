/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccount;
import com.khoders.tsm.entities.system.UserAccountRecord;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author richard
 */
@Entity
@Table(name = "stock_receipt")
public class StockReceipt extends UserAccountRecord implements Serializable{
  
    public static final String _receiptNo="receiptNo";
    @Column(name = "receipt_no")
    private String receiptNo;
    
    public static final String _purchaseOrder="purchaseOrder";
    @JoinColumn(name = "purchase_order", referencedColumnName = "id")
    @ManyToOne
    private PurchaseOrder purchaseOrder;
    
    public static final String _location="location";
    @JoinColumn(name = "location", referencedColumnName = "id")
    @ManyToOne
    private Location location;
    
    public static final String _receivedBy="receivedBy";    
    @JoinColumn(name = "received_by", referencedColumnName = "id")
    @ManyToOne
    private UserAccount receivedBy;
    
    public static final String _batchNo="batchNo";
    @Column(name = "batch_no")
    private String batchNo;
    
    @Column(name = "stock_saved")
    private boolean stockSaved = false;
    
    public static final String _totalAmount="totalAmount";   
    @Column(name = "total_amount")
    private Double totalAmount;
    
    @Column(name = "description")
    private String description;
    
    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public UserAccount getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(UserAccount receivedBy) {
        this.receivedBy = receivedBy;
    }
    
    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public boolean isStockSaved()
    {
        return stockSaved;
    }

    public void setStockSaved(boolean stockSaved)
    {
        this.stockSaved = stockSaved;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
