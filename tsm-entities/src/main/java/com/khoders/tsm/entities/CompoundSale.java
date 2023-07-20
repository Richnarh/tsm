/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.resource.enums.PaymentStatus;
import com.khoders.tsm.entities.system.UserAccountRecord;
import java.io.Serializable;
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
@Table(name = "compound_sale")
public class CompoundSale extends UserAccountRecord implements Serializable{
    @Column(name = "compound_amount")
    private Double compoundAmount;
    
    public static final String _customer = "customer";
    @JoinColumn(name = "customer", referencedColumnName = "id")
    @ManyToOne
    private Customer customer;
    
    public static final String _paymentStatus = "paymentStatus";
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Double getCompoundAmount() {
        return compoundAmount;
    }

    public void setCompoundAmount(Double compoundAmount) {
        this.compoundAmount = compoundAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    
}
