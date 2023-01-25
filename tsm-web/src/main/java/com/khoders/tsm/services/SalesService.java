/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.khoders.tsm.services;

import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.SalesTax;
import com.khoders.tsm.entities.Tax;
import com.khoders.tsm.enums.CustomerType;
import com.khoders.tsm.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.UnitMeasurement;
import java.time.LocalDate;
import java.util.Collections;
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
          String query = "SELECT e FROM SalesTax e WHERE e.sales=?1 AND e.userAccount=?2 ORDER BY e.reOrder ASC";
        
        TypedQuery<SalesTax> typedQuery = crudApi.getEm().createQuery(query, SalesTax.class)
                                .setParameter(1, sales)
                                .setParameter(2, appSession.getCurrentUser());
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
    
    public List<Sales> getUnapprovaedSales()
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.approval=:approval ORDER BY e.valueDate DESC", Sales.class)
                    .setParameter("approval", false)
                    .getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    
    public Customer walkinCustomer()
    {
        String qryString = "SELECT e FROM Customer e WHERE e.customerName=?1";
        TypedQuery<Customer> typedQuery = crudApi.getEm().createQuery(qryString, Customer.class)
                .setParameter(1, CustomerType.WALK_IN_CUSTOMER.getLabel());
        return typedQuery.getResultStream().findFirst().orElse(null);
    }
    
    public List<SaleItem> getSales(Sales sales)
    {
        try
        {
           TypedQuery<SaleItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM SaleItem e WHERE e.sales=:sales", SaleItem.class);
                        typedQuery.setParameter("sales", sales);
                       return typedQuery.getResultList();
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<Sales> getSales()
    {
        try
        {
           TypedQuery<Sales> typedQuery = crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.valueDate BETWEEN ?1 AND ?2", Sales.class)
                   .setParameter(1, LocalDate.now())
                   .setParameter(2, LocalDate.now());
                    
           return  typedQuery.getResultList();
           
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
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
            return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.stockReceiptItem =:receiptItem", Inventory.class)
                    .setParameter("receiptItem", receiptItem)
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
            return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.unitMeasurement = ?1 AND e.stockReceiptItem = ?2", Inventory.class)
                    .setParameter(1, unitMeasurement)
                    .setParameter(2, StockReceiptItem)
                    .getSingleResult().getPackagePrice();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0.0;
    }
}
