/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.dto;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author richa
 */
public class InvoiceDto
{
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String companyName;
    private String telNumber;
    private String receiptNumber;
    private String invoiceType;
    private String companyAddress;
    private String website;
    private String invoiceNotes;
    
    private double subTotalAmount;
    private double totalAmount;
    private double totalPayable;
    private String paymentMethod;
    
    private String customerName;
    private String customerId;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    
    private List<SaleItemDto> saleItemList = new LinkedList<>();  
    public List<SalesTaxDto> taxList = new LinkedList<>();

    public LocalDate getIssueDate()
    {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate)
    {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate)
    {
        this.expiryDate = expiryDate;
    }
    
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getTelNumber()
    {
        return telNumber;
    }

    public void setTelNumber(String telNumber)
    {
        this.telNumber = telNumber;
    }

    public String getReceiptNumber()
    {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber)
    {
        this.receiptNumber = receiptNumber;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod()
    {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }
    
    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public List<SaleItemDto> getSaleItemList()
    {
        return saleItemList;
    }

    public void setSaleItemList(List<SaleItemDto> saleItemList)
    {
        this.saleItemList = saleItemList;
    }

    public double getTotalPayable()
    {
        return totalPayable;
    }

    public void setTotalPayable(double totalPayable)
    {
        this.totalPayable = totalPayable;
    }  

    public String getInvoiceType()
    {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType)
    {
        this.invoiceType = invoiceType;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    public String getCompanyAddress()
    {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress)
    {
        this.companyAddress = companyAddress;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public double getSubTotalAmount()
    {
        return subTotalAmount;
    }

    public void setSubTotalAmount(double subTotalAmount)
    {
        this.subTotalAmount = subTotalAmount;
    }

    public List<SalesTaxDto> getTaxList()
    {
        return taxList;
    }

    public void setTaxList(List<SalesTaxDto> taxList)
    {
        this.taxList = taxList;
    }

    public String getInvoiceNotes()
    {
        return invoiceNotes;
    }

    public void setInvoiceNotes(String invoiceNotes)
    {
        this.invoiceNotes = invoiceNotes;
    }
    
}
