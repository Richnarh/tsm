/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.dto;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Pascal
 */
public class Waybill extends SalesReceipt{
    private String carNo;
    private String driverName;
    private LocalDate deliveryDate;
    private String customer;
    private String address;

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<SalesTaxDto> getTaxList() {
        return taxList;
    }

    public void setTaxList(List<SalesTaxDto> taxList) {
        this.taxList = taxList;
    }
    
}
