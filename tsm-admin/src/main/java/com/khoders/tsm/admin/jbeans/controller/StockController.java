/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.resource.utilities.FormView;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.enums.ListingType;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Richard Narh
 */
@Named(value = "stockController")
@SessionScoped
public class StockController implements Serializable{
    private ListingType listingType;
    private List<StockReceipt> stockReceiptList = new LinkedList<>();
    private FormView pageView = FormView.listForm();
    
    
    public void initStock(){
        pageView.restToCreateView();
    }
    public void editWarehouseStock(StockReceipt stockReceipt){
        
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
    
}
