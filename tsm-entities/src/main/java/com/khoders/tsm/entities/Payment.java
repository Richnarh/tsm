/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.resource.enums.PaymentMethod;
import com.khoders.tsm.entities.system.UserAccountRecord;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "payment")
public class Payment extends UserAccountRecord{
    
    public static final String _paymentMethod = "paymentMethod";  
    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.CASH;
    
    public static final String _amountPaid = "amountPaid";  
    @Column(name = "amount_paid")
    private Double amountPaid;
    
    public static final String _sales = "sales"; 
    @JoinColumn(name = "sales", referencedColumnName = "id")
    @ManyToOne
    private Sales sales;

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }
    
    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }
    
}
