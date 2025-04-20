/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.dolphindoors.resource.enums.PaymentMethod;
import com.dolphindoors.resource.enums.PaymentStatus;
import com.tsm.entities.system.UserAccountRecord;
import java.time.LocalDate;
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
 * @author Pascal
 */
@Entity
@Table(name = "credit_payment")
public class CreditPayment extends UserAccountRecord{
    
    public static final String _customer = "customer";
    @JoinColumn(name = "customer", referencedColumnName = "id")
    @ManyToOne
    private Customer customer;
    
    @Column(name = "description")
    @Lob
    private String description;
    
    public static final String _compoundSale = "compoundSale";
    @JoinColumn(name = "compound_sale", referencedColumnName = "id")
    @ManyToOne
    private CompoundSale compoundSale;
    
    public static final String _sales = "sales";
    @JoinColumn(name = "sales", referencedColumnName = "id")
    @ManyToOne
    private Sales sales;
    
    @Column(name = "total_credit")
    private Double totalCredit;
    
    @Column(name = "credit_remaining")
    private Double creditRemaining;
    
    @Column(name = "amount_paid")
    private Double amountPaid;
    
    @Column(name = "payment_date")
    private LocalDate paymentDate = LocalDate.now();
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    public static final String _paymentStatus = "paymentStatus";
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    public static final String _paymentMethod = "paymentMethod";
    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.CASH;
    
    @Column(name = "delete_payment")
    private boolean deletePyament;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CompoundSale getCompoundSale() {
        return compoundSale;
    }

    public void setCompoundSale(CompoundSale compoundSale) {
        this.compoundSale = compoundSale;
    }

    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }
    
    public Double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }
    
    public Double getCreditRemaining() {
        return creditRemaining;
    }

    public void setCreditRemaining(Double creditRemaining) {
        this.creditRemaining = creditRemaining;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isDeletePyament() {
        return deletePyament;
    }

    public void setDeletePyament(boolean deletePyament) {
        this.deletePyament = deletePyament;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
}
