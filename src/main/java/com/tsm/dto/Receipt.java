/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author richa
 */
public class Receipt{
    private String receiptNumber;
    private LocalDateTime date;
    private String companyName;
    private String website;
    private String modeOfPayment;
    private String customer;
    private String phoneNumber;
    
    private double totalAmount;
    private double totalPayable;
    private List<SaleItemDto> saleItemList;

    public String getReceiptNumber()
    {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber)
    {
        this.receiptNumber = receiptNumber;
    }

    public LocalDateTime getDate()
    {
        return date;
    }

    public void setDate(LocalDateTime date)
    {
        this.date = date;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getModeOfPayment()
    {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment)
    {
        this.modeOfPayment = modeOfPayment;
    }
    
    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public double getTotalPayable()
    {
        return totalPayable;
    }

    public void setTotalPayable(double totalPayable)
    {
        this.totalPayable = totalPayable;
    }

    public List<SaleItemDto> getSaleItemList() {
        return saleItemList;
    }

    public void setSaleItemList(List<SaleItemDto> saleItemList) {
        this.saleItemList = saleItemList;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
}
