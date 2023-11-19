/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.services;

import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.system.CompanyBranch;
import com.khoders.tsm.entities.system.UserAccount;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.Location;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.enums.LocType;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

/**
 *
 * @author richa
 */
@Stateless
public class StockService
{
 @Inject private CrudApi crudApi;
    
    public List<Sales> getInvoiceList(CompanyBranch companyBranch, UserAccount userAccount)
    {
        try
        {
            if(companyBranch != null || userAccount != null)
            {
                String qryString = "SELECT e FROM Sales e WHERE e.companyBranch=?1 AND e.userAccount=?2";
                TypedQuery<Sales> typedQuery = crudApi.getEm().createQuery(qryString, Sales.class)
                    .setParameter(1, companyBranch)
                    .setParameter(2, userAccount);
                return typedQuery.getResultList();  
            }
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return Collections.emptyList();
    }
    
     
    public List<Sales> getInvoiceByDates(DateRangeUtil dateRange)
    {
        try {
            if(dateRange.getFromDate() == null || dateRange.getToDate() == null)
            {
                String  queryString = "SELECT e FROM Sales e ";
                return crudApi.getEm().createQuery(queryString, Sales.class).getResultList();
            }
            
            String qryString = "SELECT e FROM Sales e WHERE e.valueDate BETWEEN ?1 AND ?2";
            
            TypedQuery<Sales> typedQuery = crudApi.getEm().createQuery(qryString, Sales.class)
                    .setParameter(1, dateRange.getFromDate())
                    .setParameter(2, dateRange.getToDate());
           return typedQuery.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
     
    public List<Sales> getSalesByBranch(CompanyBranch companyBranch)
    {
        try {
           
            String qryString = "SELECT e FROM Sales e WHERE e.companyBranch=?1";
            
            TypedQuery<Sales> typedQuery = crudApi.getEm().createQuery(qryString, Sales.class)
                    .setParameter(1, companyBranch);
           return typedQuery.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
  
     
    public List<Sales> getSalesByReceipt(String receiptNumber)
    {
        try {
           
            String qryString = "SELECT e FROM Sales e WHERE e.receiptNumber=?1";
            
            TypedQuery<Sales> typedQuery = crudApi.getEm().createQuery(qryString, Sales.class)
                    .setParameter(1, receiptNumber);
           return typedQuery.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
  
    public List<Sales> getSalesByEmployee(UserAccount userAccount)
    {
        try 
        {
         
        String qryString = "SELECT e FROM Sales e WHERE e.userAccount=?1";
            
        TypedQuery<Sales> typedQuery = crudApi.getEm().createQuery(qryString, Sales.class)
                    .setParameter(1, userAccount);
        return typedQuery.getResultList();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    
    public List<SaleItem> getSalesDetailList(Sales sales)
    {
        try
        {
           String qryString = "SELECT e FROM SaleItem e WHERE e.sales=?1";
           return crudApi.getEm().createQuery(qryString, SaleItem.class)
                   .setParameter(1, sales)
                   .getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return Collections.emptyList();
    }
    
    public List<StockReceiptItem> getStockList(CompanyBranch companyBranch){
       return crudApi.getEm().createQuery("SELECT e FROM StockReceiptItem e WHERE e.companyBranch=:companyBranch ORDER BY e.product ASC", StockReceiptItem.class)
                  .setParameter(StockReceiptItem._companyBranch, companyBranch)
                  .getResultList();
    }
    
    public List<StockReceipt> getStockList(DateRangeUtil dateRange){
        if(dateRange.getFromDate() == null && dateRange.getToDate() == null) {
            return crudApi.findAll(StockReceipt.class).stream()
                    .sorted(Comparator.comparing(StockReceipt::getValueDate).reversed())
                    .collect(Collectors.toCollection(LinkedList::new));
        }
        
        return crudApi.getEm().createQuery("SELECT e FROM StockReceipt e WHERE e.valueDate BETWEEN :valueDate AND :valueDate ORDER BY e.valueDate DESC", StockReceipt.class)
                .setParameter(StockReceipt._valueDate, dateRange.getFromDate())
                .setParameter(StockReceipt._valueDate, dateRange.getToDate())
                .getResultList();
    }
    
    public List<Inventory> getInventoryList(DateRangeUtil dateRange, CompanyBranch companyBranch){
        if(dateRange.getFromDate() == null && dateRange.getToDate() == null && companyBranch == null) {
            return crudApi.findAll(Inventory.class).stream()
                    .sorted(Comparator.comparing(Inventory::getValueDate).reversed())
                    .collect(Collectors.toCollection(LinkedList::new));
        }else if(dateRange.getFromDate() == null && dateRange.getToDate() == null && companyBranch != null) {
           return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.companyBranch =:companyBranch ORDER BY e.valueDate DESC", Inventory.class)
                .setParameter(Inventory._companyBranch, companyBranch)
                .getResultList(); 
        }else{
            return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.companyBranch =:companyBranch AND e.valueDate BETWEEN :valueDate AND :valueDate ORDER BY e.valueDate DESC", Inventory.class)
                    .setParameter(Inventory._companyBranch, companyBranch)
                    .setParameter(Inventory._valueDate, dateRange.getFromDate())
                    .setParameter(Inventory._valueDate, dateRange.getToDate())
                    .getResultList();
        }
    }
    
    public List<StockReceiptItem> getStockShortageList(CompanyBranch companyBranch){
        try
        {
          String qryString = "SELECT e FROM StockReceiptItem e WHERE e.companyBranch=?1 AND e.pkgQuantity <= 1 ORDER BY e.product ASC";
          return crudApi.getEm().createQuery(qryString, StockReceiptItem.class)
                  .setParameter(1, companyBranch)
                  .getResultList();
           
        } catch (Exception e)
        {
        }
        
        return Collections.emptyList();
    }
    
      
    public List<Sales> getSales(DateRangeUtil dateRange)
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<Sales> getTotalSumPerDateRange(DateRangeUtil dateRange)
    {
        try
        {
           return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.valueDate BETWEEN ?1 AND ?2 ", Sales.class)
                   .setParameter(1, dateRange.getFromDate())
                   .setParameter(2, dateRange.getToDate())
                   .getResultList();
           
        } catch (Exception e)
        {
        }
        
        return Collections.emptyList();
    }
    
    public List<Location> getLocationList(CompanyBranch companyBranch) {
        return crudApi.getEm().createQuery("SELECT e FROM Location e WHERE e.companyBranch = :companyBranch ORDER BY e.createdDate DESC", Location.class)
                 .setParameter(Location._companyBranch, companyBranch)
                 .getResultList();
    }

    public Location findLocationByBranch(CompanyBranch selectedBranch, LocType locType) {
       return crudApi.getEm().createQuery("SELECT e FROM Location e WHERE e.locType = :locType AND e.companyBranch = :companyBranch ORDER BY e.createdDate DESC", Location.class)
                 .setParameter(Location._locType, locType)
                 .setParameter(Location._companyBranch, selectedBranch)
                 .getResultStream().findFirst().orElse(null);
    }
}
