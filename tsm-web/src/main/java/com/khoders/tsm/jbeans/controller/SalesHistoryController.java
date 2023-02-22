/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
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
    
    private Customer selectedCustomer = null;
    
    public void loadCustomer(){
        customerList = inventoryService.getCustomerList();
    }
    
    public void loadCustomerSales(Customer customer){
        selectedCustomer = customer;
        salesList = salesService.getCustomerSales(customer);
    }
    
    public void loadSaleItem(Sales sales){
        saleItemList = salesService.getSales(sales);
    }
    
    public void resetPage(){
        selectedCustomer = null;
        saleItemList = new LinkedList<>();
        salesList = new LinkedList<>();
        customerList = new LinkedList<>();
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
    
}
