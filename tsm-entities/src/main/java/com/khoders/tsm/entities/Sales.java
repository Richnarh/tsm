/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
import com.khoders.resource.enums.PaymentMethod;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.enums.SalesType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author khoders
 */
@Entity
@Table(name = "sales")
public class Sales extends UserAccountRecord {

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @JoinColumn(name = "customer", referencedColumnName = "id")
    @ManyToOne
    private Customer customer;

    public static final String _receiptNumber = "receiptNumber";
    @Column(name = "receipt_number")
    private String receiptNumber;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.CASH;

    @Column(name = "qty_purchased")
    private double qtyPurchased;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "sales_type")
    @Enumerated(EnumType.STRING)
    private SalesType salesType = SalesType.NORMAL_SALES;

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getQtyPurchased() {
        return qtyPurchased;
    }

    public void setQtyPurchased(double qtyPurchased) {
        this.qtyPurchased = qtyPurchased;
    }

    public SalesType getSalesType() {
        return salesType;
    }

    public void setSalesType(SalesType salesType) {
        this.salesType = salesType;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public void genReceipt() {
        if (getReceiptNumber() != null) {
            setReceiptNumber(getReceiptNumber());
        } else {
            setReceiptNumber(SystemUtils.generateRefNo());
        }
    }

    @Override
    public String toString() {
        return customer+" "+receiptNumber;
    }
    
    
}
