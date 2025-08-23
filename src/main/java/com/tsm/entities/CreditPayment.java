/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.tsm.entities.system.CompanyRecord;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "credit_payment")
public class CreditPayment extends CompanyRecord{
    
    public static final String _sales = "sales";
    @JoinColumn(name = "sales", referencedColumnName = "id")
    @ManyToOne
    private Sales sales;
    
    public static final String _round = "round";
    @Column(name = "round")
    private int round;
    
    @Column(name = "amount_paid")
    private double amountPaid;
    
    @Column(name = "amount_remaining")
    private double amountRemaining;
    
    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(double amountRemaining) {
        this.amountRemaining = amountRemaining;
    }
}
