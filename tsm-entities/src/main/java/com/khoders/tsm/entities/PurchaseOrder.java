package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
import com.khoders.resource.enums.DeliveryMethod;
import com.khoders.resource.enums.PaymentMethod;
import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
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
 * @author richard
 */
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder extends UserAccountRecord implements Serializable
{
    @Column(name = "order_code")
    private String orderCode = SystemUtils.generatePO();
    
    @Column(name = "purchased_date")
    private LocalDate purchasedDate = LocalDate.now();

    @JoinColumn(name = "customer", referencedColumnName = "id")
    @ManyToOne
    private Customer customer;
    
    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.CASH;
    
    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;
    
    @Column(name = "total_amount")
    private double totalAmount;
    
    @Column(name = "stock_fully_received")
    private boolean stockFullyReceived = false;
    
    @Column(name = "description")
    @Lob
    private String description;
    
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public LocalDate getPurchasedDate()
    {
        return purchasedDate;
    }

    public void setPurchasedDate(LocalDate purchasedDate)
    {
        this.purchasedDate = purchasedDate;
    } 

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isStockFullyReceived() {
        return stockFullyReceived;
    }

    public void setStockFullyReceived(boolean stockFullyReceived) {
        this.stockFullyReceived = stockFullyReceived;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
   
    public void genOrderCode()
    {
        if (getOrderCode()!= null)
        {
            setOrderCode(getOrderCode());
        } else
        {
            setOrderCode(SystemUtils.generatePO());
        }
    }

    @Override
    public String toString()
    {
       return orderCode +" "+purchasedDate;
    }
    
}
