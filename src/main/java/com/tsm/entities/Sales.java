/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.dolphindoors.resource.enums.PaymentStatus;
import com.dolphindoors.resource.utilities.JUtils;
import com.tsm.entities.system.UserAccountRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author khoders
 */
@Entity
@Table(name = "sales")
public class Sales extends UserAccountRecord {

    @Column(name = "sales_date")
    private LocalDateTime salesDate;
    
    public static final String _customer = "customer";
    @JoinColumn(name = "customer", referencedColumnName = "id")
    @ManyToOne
    private Customer customer;

    public static final String _receiptNumber = "receiptNumber";
    @Column(name = "receipt_number")
    private String receiptNumber;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "qty_purchased")
    private Double qtyPurchased;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    public static final String _paymentStatus = "paymentStatus"; 
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    public static final String _notes = "notes";    
    @Column(name = "notes")
    @Lob
    private String notes;
    
    public Double getQtyPurchased() {
        return qtyPurchased;
    }

    public void setQtyPurchased(Double qtyPurchased) {
        this.qtyPurchased = qtyPurchased;
    }
    
    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
 
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public void genReceipt() {
        if (getReceiptNumber() != null) {
            setReceiptNumber(getReceiptNumber());
        } else {
            setReceiptNumber(JUtils.generateRefNo());
        }
    }

    @Override
    public String toString() {
        return customer+" "+receiptNumber;
    }
    
    
}
