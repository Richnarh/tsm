/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.tsm.entities.CreditPayment;
import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.SalesAdditionalInfo;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.InventoryService;
import com.khoders.tsm.services.SalesService;
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
@Named(value = "deliveriesController")
@SessionScoped
public class DeliveriesController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private SalesService salesService;
    @Inject private InventoryService inventoryService;
    
    private List<Customer> customerList = new LinkedList<>();
    private List<SalesAdditionalInfo> salesAdditionalInfoList = new LinkedList<>();
    private List<SaleItem> saleItemList = new LinkedList<>();
    private List<CreditPayment> creditPaymentList = new LinkedList<>();
    
    private Customer selectedCustomer = null;
    private double totalAmount = 0.0;
    
    public void loadCustomer(){
        customerList = inventoryService.getCustomerList();
    }
    
    public void loadCustomerSales(Customer customer){
        clear();
        selectedCustomer = customer;
//        salesAdditionalInfoList = salesService.getDeliveries(customer);
        fetchCreditPayment(customer);
    }
    
    public void loadSaleItem(Sales sales){
        saleItemList = salesService.getSales(sales);
    }
    
    public void fetchCreditPayment(Customer c){
        totalAmount = 0.0;
        creditPaymentList = salesService.getCreditPayments(c);
//        totalAmount = salesList.stream().mapToDouble(Sales::getTotalAmount).sum();
    }
    
    public void clear(){
        selectedCustomer = null;
        saleItemList = new LinkedList<>();
        creditPaymentList = new LinkedList<>();
    }
    public void resetPage(){
        selectedCustomer = null;
        saleItemList = new LinkedList<>();
        customerList = new LinkedList<>();
        creditPaymentList = new LinkedList<>();
    }
}
