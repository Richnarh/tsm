/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
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
   @Column(name = "quantity")
   private int quantity;
   
   @Column(name = "return_date")
   private LocalDate returnDate;
   
   @JoinColumn(name = "product", referencedColumnName = "id")
   @ManyToOne
   private Product product;
   
   @Column(name = "sale")
   private boolean sale;
      
   @JoinColumn(name = "customer", referencedColumnName = "id")
   @ManyToOne
   private Customer customer;
   
   @JoinColumn(name = "sales", referencedColumnName = "id")
   @ManyToOne
   private Sales sales;
   
   @JoinColumn(name = "location", referencedColumnName = "id")
   @ManyToOne
   private Location location;
   
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

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isSale() {
        return sale;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
    }

    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }
    
}
