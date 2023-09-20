/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.dto;

import java.time.LocalDateTime;

/**
 *
 * @author richa
 */
public class CashReceipt
{
    private String refNo;
    private LocalDateTime date;
    private String branchName;
    private String website;
    private String modeOfPayment;
    
    private double invoiceAmnt;
    private double amountPaid;
    private double totalTax;
    private double amountRem;

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }
    
    public LocalDateTime getDate()
    {
        return date;
    }

    public void setDate(LocalDateTime date)
    {
        this.date = date;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
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
   
    public double getTotalTax()
    {
        return totalTax;
    }

    public void setTotalTax(double totalTax)
    {
        this.totalTax = totalTax;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountRem() {
        return amountRem;
    }

    public void setAmountRem(double amountRem) {
        this.amountRem = amountRem;
    }

    public double getInvoiceAmnt() {
        return invoiceAmnt;
    }

    public void setInvoiceAmnt(double invoiceAmnt) {
        this.invoiceAmnt = invoiceAmnt;
    }
    
}
