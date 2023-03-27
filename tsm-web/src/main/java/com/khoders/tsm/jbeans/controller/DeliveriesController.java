/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.resource.enums.DeliveryStatus;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.reports.ReportManager;
import com.khoders.resource.utilities.Msg;
import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.DeliveryInfo;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.jbeans.ReportFiles;
import com.khoders.tsm.jbeans.dto.SalesReceipt;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.SalesService;
import com.khoders.tsm.services.XtractService;
import java.io.Serializable;
import java.time.LocalDate;
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
    @Inject private XtractService xtractService;
    @Inject private ReportManager reportManager;
    
    private List<Customer> customerList = new LinkedList<>();
    private List<SaleItem> saleItemList = new LinkedList<>();
    
    private Customer selectedCustomer = null;
    private double totalAmount = 0.0;
    private String receiptNumber = null;
    
    public void loadDeliveries(){
        if(receiptNumber == null)return;
        clear();
        System.out.println("receiptNumber: "+receiptNumber);
        saleItemList = salesService.getDeliveries(receiptNumber.trim());
    }
    
    public void saveDeliveryInfo(SaleItem saleItem){
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setSaleItem(saleItem);
        deliveryInfo.setDeliveryDate(LocalDate.now());
        deliveryInfo.setDeliveryStatus(DeliveryStatus.DELIVERED);
        deliveryInfo.setReceiptNumber(receiptNumber);
        deliveryInfo.setCompanyBranch(appSession.getCompanyBranch());
        deliveryInfo.setUserAccount(appSession.getCurrentUser());
        deliveryInfo.genCode();
        
        crudApi.save(deliveryInfo);
        saleItem.setDeliveryStatus(DeliveryStatus.DELIVERED);
       crudApi.save(saleItem);
    }
    
    public void printWaybill(){
        try
        {
            List<SalesReceipt> salesReceiptList = new LinkedList<>();
            
            List<DeliveryInfo> deliveryInfos = salesService.getWaybills(receiptNumber.trim());
            Sales sales = salesService.getSale(receiptNumber);
            if(deliveryInfos.isEmpty()){
                Msg.error("Receipt No. incorrect or no delivery info exist.");
                return;
            }
            
            SalesReceipt salesReceipt = xtractService.extractWaybill(deliveryInfos,sales);

            salesReceiptList.add(salesReceipt);
            ReportManager.reportParams.put("logo", ReportFiles.LOGO);
            reportManager.createReport(salesReceiptList, ReportFiles.WAYBILL_FILE, ReportManager.reportParams);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clear(){
        selectedCustomer = null;
        saleItemList = new LinkedList<>();
    }
    public void resetPage(){
        selectedCustomer = null;
        saleItemList = new LinkedList<>();
        customerList = new LinkedList<>();
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public List<SaleItem> getSaleItemList() {
        return saleItemList;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
    
    
}
