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
import com.khoders.tsm.DefaultService;
import com.khoders.tsm.admin.services.StockService;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.Location;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.system.CompanyBranch;
import com.khoders.tsm.enums.ListingType;
import com.khoders.tsm.enums.LocType;
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
    @Inject private DefaultService ds;
    
    private ListingType listingType;
    private List<StockReceipt> stockReceiptList = new LinkedList<>();
    private List<StockReceiptItem> stockReceiptItemtList = new LinkedList<>();
    private List<Inventory> inventorytList = new LinkedList<>();
    private FormView pageView = FormView.listForm();
    
    private StockReceipt stockReceipt = new StockReceipt();
    private CompanyBranch companyBranch;
    private StockReceiptItem receiptItem = new StockReceiptItem();
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
    public void initInventoryListing(){
        inventorytList = stockService.getInventoryList(dateRange, companyBranch);
    }
    
    public void saveStockReceipt(){
        try {
            stockReceipt.setLocation(ds.getObj(Location.class, Location._mainWarehouse, Location._locType, true,LocType.WAREHOUSE));
            stockReceipt.setBatchNo(SystemUtils.generateCode());
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
    
    public void addEditStockReceipt(StockReceipt stockReceipt){
        this.stockReceipt = stockReceipt;
        stockReceiptItemtList = ds.getObjList(StockReceiptItem.class, StockReceiptItem._stockReceipt, stockReceipt);
        this.optionText = "Update";
        pageView.restToDetailView();
    }
    
    public void clearStockReceipt(){
        stockReceipt = new StockReceipt();
        optionText = "Save Changes";
        stockReceiptList = new LinkedList<>();
        SystemUtils.resetJsfUI();
    }
    
    public void closePage(){
       stockReceipt = new StockReceipt();
       optionText = "Save Changes";
       pageView.restToListView();
    }
    
    public void clearStockPage(){
        receiptItem = new StockReceiptItem();
        SystemUtils.resetJsfUI();
    }
    
    public void closeStockPage(){
       clearStockPage();
       stockReceiptItemtList = new LinkedList<>();
       pageView.restToListView();
    }
    
    public void addStockItem(){
        System.out.println("receiptItem: "+receiptItem);
        if (receiptItem.getPkgQuantity() <= 0) {
            Msg.error("Please enter quantity");
            return;
        }
        System.out.println("start...");
        if (receiptItem != null) {
            System.out.println("Adding...........");
            receiptItem.genCode();
            receiptItem.setId(crudApi.genId());
            receiptItem.setStockReceipt(stockReceipt);
            stockReceiptItemtList.add(receiptItem);
            stockReceiptItemtList = CollectionList.washList(stockReceiptItemtList, receiptItem);
            System.out.println("");
            Msg.info("One item added to cart");
        }
        clearStockPage();
    }
    
    
    public void saveAll(){
        if(stockReceiptItemtList.isEmpty()){
            Msg.error("No item to save.");
            return;
        }
        for (StockReceiptItem stockReceiptItem : stockReceiptItemtList) {
            stockReceiptItem.genCode();
            stockReceiptItem.setStockReceipt(stockReceipt);
            crudApi.save(stockReceiptItem);
        }
        Msg.info("Stock Item saved");
    }
    
    public void clearItem(){
        
    }
    
    public void editStockItem(StockReceiptItem receiptItem){
        
    }
    
    public void removeStockItem(StockReceiptItem receiptItem){
        
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

    public StockReceiptItem getReceiptItem() {
        return receiptItem;
    }

    public void setReceiptItem(StockReceiptItem receiptItem) {
        this.receiptItem = receiptItem;
    }

    public List<StockReceiptItem> getStockReceiptItemtList() {
        return stockReceiptItemtList;
    }

    public List<Inventory> getInventorytList() {
        return inventorytList;
    }

    public CompanyBranch getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch) {
        this.companyBranch = companyBranch;
    }
}
