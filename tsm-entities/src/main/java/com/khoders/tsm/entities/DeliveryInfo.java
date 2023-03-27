/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.resource.enums.DeliveryStatus;
import com.khoders.tsm.entities.system.UserAccountRecord;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "delivery_info")
public class DeliveryInfo extends UserAccountRecord {
    public static final String _saleItem = "saleItem";
    @JoinColumn(name = "sale_item", referencedColumnName = "id")
    @ManyToOne
    private SaleItem saleItem;
    
    public static final String _receiptNumber = "receiptNumber";
    @Column(name = "receipt_number")
    private String receiptNumber;
    
    public static final String _deliveryStatus = "deliveryStatus";
    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    
    public static final String _deliveryDate = "deliveryDate";
    @Column(name = "delivery_date")
    private LocalDate deliveryDate;
    
    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
    
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }    

    public SaleItem getSaleItem() {
        return saleItem;
    }

    public void setSaleItem(SaleItem saleItem) {
        this.saleItem = saleItem;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
    
}
