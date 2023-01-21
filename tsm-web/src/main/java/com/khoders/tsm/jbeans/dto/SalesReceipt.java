package com.khoders.tsm.jbeans.dto;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author pascal
 */
public class SalesReceipt
{
    private String receiptNumber;
    private LocalDateTime date;
    private String website;
    private String modeOfPayment;
    private String branchName;
    
    private double totalAmount;
    private double totalTax;
    private String cashier;
    private double totalPayable;
    private String phoneNumber;
    
    private List<SaleItemDto> saleItemList = new LinkedList<>();  
    public List<SalesTaxDto> taxList = new LinkedList<>();

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
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

    public List<SaleItemDto> getSaleItemList()
    {
        return saleItemList;
    }

    public void setSaleItemList(List<SaleItemDto> saleItemList)
    {
        this.saleItemList = saleItemList;
    }

    public List<SalesTaxDto> getTaxList()
    {
        return taxList;
    }

    public void setTaxList(List<SalesTaxDto> taxList)
    {
        this.taxList = taxList;
    }
    
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
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

    public double getTotalTax()
    {
        return totalTax;
    }

    public void setTotalTax(double totalTax)
    {
        this.totalTax = totalTax;
    }

    public String getCashier()
    {
        return cashier;
    }

    public void setCashier(String cashier)
    {
        this.cashier = cashier;
    }

    public double getTotalPayable()
    {
        return totalPayable;
    }

    public void setTotalPayable(double totalPayable)
    {
        this.totalPayable = totalPayable;
    }
}
