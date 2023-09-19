/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
import com.khoders.resource.utilities.SystemUtils;
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
@Table(name = "transfer_item")
public class TransferItem extends UserAccountRecord implements Serializable{
    @Column(name = "transfer_code")
    private String transferCode = SystemUtils.generateShortCode();
    
    @JoinColumn(name = "stock_receipt_item")
    @ManyToOne
    private StockReceiptItem stockReceiptItem;
    
    public static final String _batchTransfer = "batchTransfer";
    @JoinColumn(name = "batch_transfer")
    @ManyToOne
    private BatchTransfer batchTransfer;
    
    @Column(name = "qty_transferred")
    private Double qtyTransferred;
        
    public String getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(String transferCode) {
        this.transferCode = transferCode;
    }

    public StockReceiptItem getStockReceiptItem() {
        return stockReceiptItem;
    }

    public void setStockReceiptItem(StockReceiptItem stockReceiptItem) {
        this.stockReceiptItem = stockReceiptItem;
    }

    public Double getQtyTransferred() {
        return qtyTransferred;
    }

    public void setQtyTransferred(Double qtyTransferred) {
        this.qtyTransferred = qtyTransferred;
    }
    
    public BatchTransfer getBatchTransfer() {
        return batchTransfer;
    }

    public void setBatchTransfer(BatchTransfer batchTransfer) {
        this.batchTransfer = batchTransfer;
    }
    
}
