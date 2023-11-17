/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.resource.utilities.FormView;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.admin.services.StockService;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.enums.ListingType;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Richard Narh
 */
@Named(value = "stockController")
@SessionScoped
public class StockController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private StockService stockService;
    private ListingType listingType;
    private List<StockReceipt> stockReceiptList = new LinkedList<>();
    private FormView pageView = FormView.listForm();
    
    private StockReceipt stockReceipt = new StockReceipt();
    private DateRangeUtil dateRange = new DateRangeUtil();
    private String optionText;
    
    @PostConstruct
    public void init() {
        clearStockReceipt();
    }
    
    public void initStock(){
        clearStockReceipt();
        pageView.restToCreateView();
    }
    
    public void initStockListing(){
        stockReceiptList = stockService.getStockList(dateRange);
    }
    
    public void saveStockReceipt(){
        try {
            if(crudApi.save(stockReceipt) != null){
                stockReceiptList = CollectionList.washList(stockReceiptList, stockReceipt);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
            closePage();
        } catch (Exception e) {
        }
    }
    public void deleteProduct(StockReceipt stockReceipt) {
         if (crudApi.delete(stockReceipt)) {
            stockReceiptList.remove(stockReceipt);
            Msg.info(Msg.DELETE_MESSAGE);
        } else {
            Msg.error(Msg.FAILED_MESSAGE);
        }
    }
    public void editStockReceipt(StockReceipt stockReceipt){
        pageView.restToCreateView();
        this.stockReceipt = stockReceipt;
        this.optionText = "Update";
    }
    
    public void clearStockReceipt(){
        stockReceipt = new StockReceipt();
        optionText = "Save Changes";
        stockReceiptList = new LinkedList<>();
        SystemUtils.resetJsfUI();
    }
    public void closePage(){
       clearStockReceipt();
       pageView.restToListView();
    }
    public ListingType getListingType() {
        return listingType;
    }

    public void setListingType(ListingType listingType) {
        this.listingType = listingType;
    }

    public List<StockReceipt> getStockReceiptList() {
        return stockReceiptList;
    }

    public FormView getPageView() {
        return pageView;
    }

    public void setPageView(FormView pageView) {
        this.pageView = pageView;
    }

    public String getOptionText() {
        return optionText;
    }

    public StockReceipt getStockReceipt() {
        return stockReceipt;
    }

    public void setStockReceipt(StockReceipt stockReceipt) {
        this.stockReceipt = stockReceipt;
    }

    public DateRangeUtil getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRangeUtil dateRange) {
        this.dateRange = dateRange;
    }
    
}
