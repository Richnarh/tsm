/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.jbeans.controller;

import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.entities.CreditPayment;
import com.tsm.entities.Customer;
import com.tsm.entities.SaleItem;
import com.tsm.entities.Sales;
import com.tsm.listener.AppSession;
import com.tsm.services.InventoryService;
import com.tsm.services.SalesService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Pascal
 */
@Named(value = "salesHistoryController")
@SessionScoped
public class SalesHistoryController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private SalesService salesService;
    @Inject private InventoryService inventoryService;
    
    private List<Customer> customerList = new LinkedList<>();
    private List<Sales> salesList = new LinkedList<>();
    private List<SaleItem> saleItemList = new LinkedList<>();
    private List<CreditPayment> creditPaymentList = new LinkedList<>();
    
    private Customer selectedCustomer = null;
    private double totalAmount = 0.0;
    private String receiptNo;
    
    public void loadCustomer(){
        customerList = inventoryService.getCustomerList();
    }
    
    public void loadCustomerSales(Customer customer){
        clear();
        selectedCustomer = customer;
        salesList = salesService.getCustomerSales(customer);
        fetchCreditPayment(customer);
    }
    
    public void loadSaleItem(Sales sales){
        saleItemList = salesService.getSales(sales);
        receiptNo = sales.getReceiptNumber();
    }
    
    public void fetchCreditPayment(Customer c){
        totalAmount = 0.0;
        creditPaymentList = salesService.getCreditPayments(c);
        totalAmount = salesList.stream().mapToDouble(Sales::getTotalAmount).sum();
    }
    
    public void clear(){
        selectedCustomer = null;
        saleItemList = new LinkedList<>();
        salesList = new LinkedList<>();
        creditPaymentList = new LinkedList<>();
    }
    public void resetPage(){
        selectedCustomer = null;
        saleItemList = new LinkedList<>();
        salesList = new LinkedList<>();
        customerList = new LinkedList<>();
        creditPaymentList = new LinkedList<>();
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public List<Sales> getSalesList() {
        return salesList;
    }
    
    public List<SaleItem> getSaleItemList() {
        return saleItemList;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public List<CreditPayment> getCreditPaymentList() {
        return creditPaymentList;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getReceiptNo() {
        return receiptNo;
    }
    
}
