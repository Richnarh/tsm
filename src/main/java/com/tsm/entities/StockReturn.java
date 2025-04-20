/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.tsm.entities.system.UserAccountRecord;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "stock_return")
public class StockReturn extends UserAccountRecord implements Serializable
{   
    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "receipt_number")
    private String receiptNumber;
      
   @JoinColumn(name = "customer", referencedColumnName = "id")
   @ManyToOne
   private Customer customer;
   
   @JoinColumn(name = "sales", referencedColumnName = "id")
   @ManyToOne
   private Sales sales;
   
   @Column(name = "description")
   @Lob
   private String description;

    public LocalDate getReturnDate()
    {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate)
    {
        this.returnDate = returnDate;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
    
}
