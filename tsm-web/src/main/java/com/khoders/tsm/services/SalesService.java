/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.khoders.tsm.services;

import com.khoders.resource.enums.PaymentStatus;
import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.SalesTax;
import com.khoders.tsm.entities.Tax;
import com.khoders.tsm.enums.CustomerType;
import com.khoders.tsm.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.tsm.entities.CompoundSale;
import com.khoders.tsm.entities.CreditPayment;
import com.khoders.tsm.entities.DeliveryInfo;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.Payment;
import com.khoders.tsm.entities.ShippingInfo;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.UnitMeasurement;
import com.khoders.tsm.enums.SaleSource;
import com.khoders.tsm.enums.SalesType;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

/**
 *
 * @author richa
 */
@Stateless
public class SalesService
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;

    public List<SalesTax> getSalesTaxList(Sales sales){
      return crudApi.getEm().createQuery("SELECT e FROM SalesTax e WHERE e.sales = :sales AND e.userAccount = :userAccount ORDER BY e.reOrder ASC", SalesTax.class)
              .setParameter(SalesTax._sales, sales)
              .setParameter(SalesTax._userAccount, appSession.getCurrentUser())
              .getResultList();
    }
    
    public List<Tax> getTaxList(){
       return crudApi.getEm().createQuery("SELECT e FROM Tax e ORDER BY e.reOrder ASC", Tax.class).getResultList();
    }
        
    public Customer walkinCustomer() {
        return crudApi.getEm().createQuery("SELECT e FROM Customer e WHERE e.customerName = :customerName", Customer.class)
                .setParameter(Customer._customerName, CustomerType.WALK_IN_CUSTOMER.getLabel())
                .getResultStream().findFirst().orElse(null);
    }
    
    public Customer backLogSupplier() {
        return crudApi.getEm().createQuery("SELECT e FROM Customer e WHERE e.customerName = :customerName", Customer.class)
                .setParameter(Customer._customerName, CustomerType.BACK_LOG_SUPPLIER.getLabel())
                .getResultStream().findFirst().orElse(null);
    }
    
    public List<SaleItem> getSales(Sales sales){
        return crudApi.getEm().createQuery("SELECT e FROM SaleItem e WHERE e.sales=:sales AND e.companyBranch = :companyBranch", SaleItem.class)
                        .setParameter(SaleItem._sales, sales)
                        .setParameter(SaleItem._companyBranch, appSession.getCompanyBranch())
                        .getResultList();
    }
    public Sales getSale(String receiptNumber){
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.receiptNumber = :receiptNumber AND e.companyBranch = :companyBranch", Sales.class)
                        .setParameter(Sales._receiptNumber, receiptNumber)
                        .setParameter(SaleItem._companyBranch, appSession.getCompanyBranch())
                        .getResultStream().findFirst().orElse(null);
    }
    
    public List<Sales> getSales(SaleSource saleSource){
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.valueDate BETWEEN :valueDate AND :valueDate AND e.saleSource = :saleSource AND e.companyBranch = :companyBranch", Sales.class)
                   .setParameter(Sales._valueDate, LocalDate.now())
                   .setParameter(Sales._valueDate, LocalDate.now())
                   .setParameter(Sales._saleSource, saleSource)
                   .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                   .getResultList();
    }
    
    public List<Sales> getSalesByDates(DateRangeUtil dateRange, SaleSource saleSource){
        if(dateRange.getFromDate() == null || dateRange.getToDate() == null) {
            return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.saleSource = :saleSource AND e.companyBranch = :companyBranch ORDER BY e.valueDate DESC", Sales.class)
                    .setParameter(Sales._saleSource, saleSource)
                    .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
        }
            
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.saleSource = :saleSource AND e.valueDate BETWEEN :valueDate AND :valueDate AND e.companyBranch = :companyBranch ORDER BY e.valueDate DESC", Sales.class)
                .setParameter(Sales._saleSource, saleSource)
                .setParameter(Sales._valueDate, dateRange.getFromDate())
                .setParameter(Sales._valueDate, dateRange.getToDate())
                .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    
    public List<Inventory> queryPackagePrice(StockReceiptItem receiptItem){
         return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.stockReceiptItem =:stockReceiptItem AND e.companyBranch = :companyBranch", Inventory.class)
                 .setParameter(Inventory._stockReceiptItem, receiptItem)
                 .setParameter(Inventory._companyBranch, appSession.getCompanyBranch())
                 .getResultList();
    }
    
    public Inventory queryPackagePrice(UnitMeasurement unitMeasurement, StockReceiptItem StockReceiptItem){
        return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.unitMeasurement = :unitMeasurement AND e.stockReceiptItem = :StockReceiptItem AND e.companyBranch = :companyBranch", Inventory.class)
                .setParameter(Inventory._unitMeasurement, unitMeasurement)
                .setParameter(Inventory._stockReceiptItem, StockReceiptItem)
                .setParameter(Inventory._companyBranch, appSession.getCompanyBranch())
                .getResultStream().findFirst().orElse(null);
    }

    public List<CreditPayment> getCreditSales(CompoundSale compoundSale) {
        return crudApi.getEm().createQuery("SELECT e FROM CreditPayment e WHERE e.compoundSale = :compoundSale AND e.companyBranch = :companyBranch ORDER BY e.paymentDate DESC", CreditPayment.class)
                    .setParameter(CreditPayment._compoundSale, compoundSale)
                    .setParameter(CreditPayment._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
    }
    public List<CreditPayment> getCreditSales(Sales sales) {
        return crudApi.getEm().createQuery("SELECT e FROM CreditPayment e WHERE e.sales = :sales AND e.companyBranch = :companyBranch ORDER BY e.paymentDate DESC", CreditPayment.class)
                .setParameter(CreditPayment._sales, sales)
                .setParameter(CreditPayment._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }

    public List<Sales> getCompoundSales(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.customer = :customer AND e.salesType = :salesType AND e.compound = :compound AND e.companyBranch = :companyBranch", Sales.class)
                .setParameter(Sales._customer, customer)
                .setParameter(Sales._salesType, SalesType.CREDIT_SALES)
                .setParameter(Sales._compound, false)
                .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    
    public CompoundSale getCompoundSale(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM CompoundSale e WHERE e.customer = :customer AND e.paymentStatus <> :paymentStatus AND e.companyBranch = :companyBranch", CompoundSale.class)
                .setParameter(CompoundSale._customer, customer)
                .setParameter(CompoundSale._paymentStatus, PaymentStatus.FULLY_PAID)
                .setParameter(CompoundSale._companyBranch, appSession.getCompanyBranch())
                .getResultStream().findFirst().orElse(null);
    }

    public List<CompoundSale> getCompoundSales() {
        return crudApi.getEm().createQuery("SELECT e FROM CompoundSale e WHERE e.companyBranch = :companyBranch", CompoundSale.class)
                .setParameter(CompoundSale._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    
    public Sales getCreditSales(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.customer = :customer AND e.salesType = :salesType AND e.compound = :compound AND e.companyBranch = :companyBranch", Sales.class)
                .setParameter(Sales._customer, customer)
                .setParameter(Sales._salesType, SalesType.CREDIT_SALES)
                .setParameter(Sales._compound, false)
                .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                .getResultStream().findFirst().orElse(null);
    }
    
    public List<Sales> getCustomerSales(Customer selectedCustomer) {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.customer = :customer AND  e.companyBranch = :companyBranch", Sales.class)
                    .setParameter(Sales._customer, selectedCustomer)
                    .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
    }

    public List<CreditPayment> getCreditPayments(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM CreditPayment e WHERE e.customer = :customer AND e.companyBranch = :companyBranch", CreditPayment.class)
                .setParameter(CreditPayment._customer, customer)
                .setParameter(CreditPayment._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    
    public Customer defaultCustomer(CustomerType customerType){
        return crudApi.getEm().createQuery("SELECT e FROM Customer e WHERE e.customerName= :customerName", Customer.class)
                .setParameter(Customer._customerName, customerType.getLabel())
                .getResultStream().findFirst().orElse(null);
    }

    public List<SaleItem> getDeliveries(String receiptNumber) {
        Sales sales = getSale(receiptNumber);
        return getSales(sales);
    }
    public List<DeliveryInfo> getWaybills(String receiptNumber) {
        return crudApi.getEm().createQuery("SELECT e FROM DeliveryInfo e WHERE e.receiptNumber = :receiptNumber", DeliveryInfo.class)
                .setParameter(DeliveryInfo._receiptNumber, receiptNumber)
                .getResultList();
    }

    public ShippingInfo getShippingInfo(String receiptNumber) {
        return crudApi.getEm().createQuery("SELECT e FROM ShippingInfo e WHERE e.receiptNumber= :receiptNumber", ShippingInfo.class)
                .setParameter(ShippingInfo._receiptNumber, receiptNumber)
                .getResultStream().findFirst().orElse(null);
    }

    public Double getWp(String id) {
        return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.id =:id", Inventory.class)
                .setParameter(Inventory._id, id)
                .getResultStream().findFirst().orElse(null).getWprice();
    }

    public List<Payment> payments(Sales sales) {
        if(sales == null) return new LinkedList<>();
        return crudApi.getEm().createQuery("SELECT e FROM Payment e WHERE e.sales = :sales", Payment.class)
                    .setParameter(Payment._sales, sales)
                    .getResultList();
    }
}
