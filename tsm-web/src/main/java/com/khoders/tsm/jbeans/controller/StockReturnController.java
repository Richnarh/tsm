/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.FormView;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.ReturnItem;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.StockReturn;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.InventoryService;
import com.khoders.tsm.services.StockService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author khoders
 */
@Named(value = "stockReturnController")
@SessionScoped
public class StockReturnController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private InventoryService inventoryService;
    @Inject private StockService stockService;

    private StockReturn stockReturn = new StockReturn();
    private ReturnItem returnItem = new ReturnItem();
    private List<StockReturn> stockReturnList = new LinkedList<>();
    private List<ReturnItem> returnItemList = new LinkedList<>();
    private List<SaleItem> saleItemList = new LinkedList<>();
    
    private StockReturn selectedStockReturn = null;

    private FormView pageView = FormView.listForm();
    private String optionText;

    @PostConstruct
    private void init(){
        clearStockReturn();
        stockReturnList = inventoryService.getStockReturnList();
    }

    public void initStockReturn()
    {
        clearStockReturn();
        pageView.restToCreateView();
    }
    
    public void manageItem(StockReturn stockReturn)
    {
        clearReturnItem();
        selectedStockReturn = stockReturn;
        returnItemList = inventoryService.getReturnItems(stockReturn);
        
        saleItem(stockReturn.getReceiptNumber());
        
        pageView.restToDetailView();
    }
    
    public void saleItem(String receiptNumber){
        Sales sale = stockService.getSales(receiptNumber);
        saleItemList = stockService.getSales(sale);
    }

    public void saveStockReturn()
    {
        try
        {
            if(stockReturn.getReceiptNumber() != null){
                Sales sales = stockService.getSales(stockReturn.getReceiptNumber());
                if(sales == null){
                    Msg.error("There are no records matching the receipt No.");
                    return;
                }else{
                    stockReturn.setSales(sales);
                    stockReturn.setCustomer(sales.getCustomer());
                }
            }
            if (crudApi.save(stockReturn) != null)
            {
                stockReturnList = CollectionList.washList(stockReturnList, stockReturn);
                Msg.info("Return good saved!");
            } else
            {
              Msg.info(Msg.FAILED_MESSAGE);
            }
            clearStockReturn();
            closePage();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteStockReturn(StockReturn stockReturn)
    {
        try
        {
            if (crudApi.delete(stockReturn))
            {
                stockReturnList.remove(stockReturn);

               Msg.info(Msg.SUCCESS_MESSAGE);
            } else
            {
             Msg.info(Msg.FAILED_MESSAGE);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void saveReturnItem(){
        try
        {
            returnItem.setStockReturn(selectedStockReturn);
            if (crudApi.save(returnItem) != null)
            {
                returnItemList = CollectionList.washList(returnItemList, returnItem);
                
                Inventory newInventory = stockService.existProdctPackage(returnItem.getSaleItem().getInventory().getStockReceiptItem(), returnItem.getSaleItem().getInventory().getUnitMeasurement().getUnits());
                double qtyInShop = newInventory.getQtyInShop();
                newInventory.setQtyInShop(qtyInShop+returnItem.getQtyReturn());
                crudApi.save(newInventory);
                Msg.info("Return item saved!");
            } else
            {
              Msg.error(Msg.FAILED_MESSAGE);
            }
            clearReturnItem();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void editReturnItem(ReturnItem returnItem){
        this.returnItem = returnItem;
        optionText = "Update";
    }
    
    public void clearReturnItem(){
        returnItem = new ReturnItem();
        returnItem.setUserAccount(appSession.getCurrentUser());
        returnItem.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }
    
    public void closeItemPage(){
        returnItemList = new LinkedList<>();   
        clearReturnItem();
        pageView.restToListView();
    }

    public void editStockReturn(StockReturn stockReturn){
        pageView.restToCreateView();
        this.stockReturn = stockReturn;
        optionText = "Update";
    }

    public void clearStockReturn(){
        stockReturn = new StockReturn();
        stockReturn.setUserAccount(appSession.getCurrentUser());
        stockReturn.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public void closePage(){
        stockReturn = new StockReturn();
        optionText = "Save Changes";
        pageView.restToListView();
    }

    public List<StockReturn> getStockReturnList()
    {
        return stockReturnList;
    }

    public StockReturn getStockReturn()
    {
        return stockReturn;
    }

    public void setStockReturn(StockReturn bird)
    {
        this.stockReturn = bird;
    }

    public String getOptionText()
    {
        return optionText;
    }

    public void setOptionText(String optionText)
    {
        this.optionText = optionText;
    }

    public FormView getPageView()
    {
        return pageView;
    }

    public void setPageView(FormView pageView)
    {
        this.pageView = pageView;
    }

    public List<ReturnItem> getReturnItemList() {
        return returnItemList;
    }

    public ReturnItem getReturnItem() {
        return returnItem;
    }

    public void setReturnItem(ReturnItem returnItem) {
        this.returnItem = returnItem;
    }

    public List<SaleItem> getSaleItemList() {
        return saleItemList;
    }
    
}
