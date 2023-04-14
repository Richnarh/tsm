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
@Table(name = "return_item")
public class ReturnItem extends UserAccountRecord implements Serializable {
    
    public static final String _stockReturn = "stockReturn";
    @JoinColumn(name = "stock_return", referencedColumnName = "id")
    @ManyToOne
    private StockReturn stockReturn;
    
    public static final String _saleItem = "saleItem";
    @JoinColumn(name = "sale_item", referencedColumnName = "id")
    @ManyToOne
    private SaleItem saleItem;
    
    public static final String _qtyReturn = "qtyReturn";
    @Column(name = "qty_return")
    private Double qtyReturn;

    public StockReturn getStockReturn() {
        return stockReturn;
    }

    public void setStockReturn(StockReturn stockReturn) {
        this.stockReturn = stockReturn;
    }

    public Double getQtyReturn() {
        return qtyReturn;
    }

    public void setQtyReturn(Double qtyReturn) {
        this.qtyReturn = qtyReturn;
    }

    public SaleItem getSaleItem() {
        return saleItem;
    }

    public void setSaleItem(SaleItem saleItem) {
        this.saleItem = saleItem;
    }
    
}
