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
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.UnitMeasurement;
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

    public List<SalesTax> getSalesTaxList(Sales sales)
    {
        try
        {
          String query = "SELECT e FROM SalesTax e WHERE e.sales = :sales AND e.userAccount = :userAccount ORDER BY e.reOrder ASC";
        
        TypedQuery<SalesTax> typedQuery = crudApi.getEm().createQuery(query, SalesTax.class)
                                .setParameter(SalesTax._sales, sales)
                                .setParameter(SalesTax._userAccount, appSession.getCurrentUser());
                return typedQuery.getResultList();      
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<Tax> getTaxList()
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM Tax e ORDER BY e.reOrder ASC", Tax.class).getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
        
    public Customer walkinCustomer()
    {
        String qryString = "SELECT e FROM Customer e WHERE e.customerName = :customerName";
        TypedQuery<Customer> typedQuery = crudApi.getEm().createQuery(qryString, Customer.class)
                .setParameter(Customer._customerName, CustomerType.WALK_IN_CUSTOMER.getLabel());
        return typedQuery.getResultStream().findFirst().orElse(null);
    }
    
    public List<SaleItem> getSales(Sales sales){
        return crudApi.getEm().createQuery("SELECT e FROM SaleItem e WHERE e.sales=:sales", SaleItem.class)
                        .setParameter(SaleItem._sales, sales)
                        .getResultList();
    }
    public Sales getSale(String receiptNumber){
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.receiptNumber = :receiptNumber", Sales.class)
                        .setParameter(Sales._receiptNumber, receiptNumber)
                        .getResultStream().findFirst().orElse(null);
    }
    
    public List<Sales> getSales()
    {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.valueDate BETWEEN ?1 AND ?2", Sales.class)
                   .setParameter(1, LocalDate.now())
                   .setParameter(2, LocalDate.now())
                   .getResultList();
    }
    public List<Sales> getSalesByDates(DateRangeUtil dateRange)
    {
        try {
            if(dateRange.getFromDate() == null || dateRange.getToDate() == null)
            {
                  String  queryString = "SELECT e FROM Sales e ORDER BY e.valueDate DESC";
                  TypedQuery<Sales> typedQuery = crudApi.getEm().createQuery(queryString, Sales.class);
                                    return typedQuery.getResultList();
            }
            
            String qryString = "SELECT e FROM Sales e WHERE e.valueDate BETWEEN ?1 AND ?2 ORDER BY e.valueDate DESC";
            
            TypedQuery<Sales> typedQuery = crudApi.getEm().createQuery(qryString, Sales.class)
                    .setParameter(1, dateRange.getFromDate())
                    .setParameter(2, dateRange.getToDate());
           return typedQuery.getResultList();
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    public List<Inventory> queryPackagePrice(StockReceiptItem receiptItem)
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.stockReceiptItem =:stockReceiptItem", Inventory.class)
                    .setParameter(Inventory._stockReceiptItem, receiptItem)
                    .getResultList();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    public double queryPackagePrice(UnitMeasurement unitMeasurement, StockReceiptItem StockReceiptItem)
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.unitMeasurement = :unitMeasurement AND e.stockReceiptItem = :StockReceiptItem", Inventory.class)
                    .setParameter(Inventory._unitMeasurement, unitMeasurement)
                    .setParameter(Inventory._stockReceiptItem, StockReceiptItem)
                    .getSingleResult().getPackagePrice();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0.0;
    }

    public List<CreditPayment> getCreditSales(Sales sales) {
        return crudApi.getEm().createQuery("SELECT e FROM CreditPayment e WHERE e.sales = :sales ORDER BY e.paymentDate DESC", CreditPayment.class)
                    .setParameter(CreditPayment._sales, sales)
                    .getResultList();
    }

    public List<Sales> getCompoundSales(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.compound = :compound AND e.customer = :customer AND e.paymentStatus <> :paymentStatus", Sales.class)
                    .setParameter(Sales._compound, true)
                    .setParameter(Sales._customer, customer)
                    .setParameter(Sales._paymentStatus, PaymentStatus.FULLY_PAID)
                    .getResultList();
    }
    
    public CompoundSale getCompoundSale(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM CompoundSale e WHERE e.customer = :customer AND e.paymentStatus <> :paymentStatus", CompoundSale.class)
                    .setParameter(CompoundSale._customer, customer)
                    .setParameter(CompoundSale._paymentStatus, PaymentStatus.PARTIALLY_PAID)
                    .getResultStream().findFirst().orElse(null);
    }

    public List<CompoundSale> getCompoundSales() {
        return crudApi.getEm().createQuery("SELECT e FROM CompoundSale e", CompoundSale.class)
                    .getResultList();
    }
    
    public Sales checkCustomerCredit(Customer customer) {
        Sales sales = crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.customer = :customer AND e.paymentStatus = :paymentStatus", Sales.class)
                    .setParameter(Sales._customer, customer)
                    .setParameter(Sales._paymentStatus, PaymentStatus.PENDING)
                    .getResultStream().findFirst().orElse(null);
        
        if(sales == null){
            crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.customer = :customer AND e.paymentStatus = :paymentStatus", Sales.class)
                    .setParameter(Sales._customer, customer)
                    .setParameter(Sales._paymentStatus, PaymentStatus.PARTIALLY_PAID)
                    .getResultStream().findFirst().orElse(null);
        }
        return sales;
    }

    public List<Sales> getDbbfSales(CompoundSale cs) {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.compoundSale = :compoundSale", Sales.class)
                    .setParameter(Sales._compoundSale, cs)
                    .getResultList();
    }

    public List<Sales> getCustomerSales(Customer selectedCustomer) {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.customer = :customer AND  e.companyBranch = :companyBranch", Sales.class)
                    .setParameter(Sales._customer, selectedCustomer)
                    .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
    }

    public List<CreditPayment> getCreditPayments(Customer customer) {
        return crudApi.getEm().createQuery("SELECT e FROM CreditPayment e WHERE e.customer = :customer AND  e.companyBranch = :companyBranch", CreditPayment.class)
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
}
