/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "shipping_info")
public class ShippingInfo extends UserAccountRecord{
    
    public static final String _receiptNumber = "receiptNumber";
    @Column(name = "receipt_number")
    private String receiptNumber;
    
    @Column(name = "driver_name")
    private String driverName;
    
    @Column(name = "car_number")
    private String carNumber;
    
    @Column(name = "notes")
    @Lob
    private String notes;

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
}
