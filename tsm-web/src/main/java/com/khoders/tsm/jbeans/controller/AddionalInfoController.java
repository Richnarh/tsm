/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.InventoryService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.SalesAdditionalInfo;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author khoders
 */
@Named(value = "addionalInfoController")
@SessionScoped
public class AddionalInfoController implements Serializable {
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private InventoryService inventoryService;

    private SalesAdditionalInfo salesAdditionalInfo = new SalesAdditionalInfo();
        
    public void popInfo(Sales sales){
        salesAdditionalInfo = null;
        salesAdditionalInfo = inventoryService.getAdditionalInfo(sales);
        if(salesAdditionalInfo == null){
            clearSalesAdditionalInfo();
            salesAdditionalInfo.setSales(sales);
            salesAdditionalInfo.genCode();
        }
    }

    public void saveSalesAdditionalInfo() {
        try {
            if (crudApi.save(salesAdditionalInfo) != null) {
                Msg.info(Msg.SUCCESS_MESSAGE);
            } else {
                Msg.error(Msg.FAILED_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void clearSalesAdditionalInfo() {
        salesAdditionalInfo = new SalesAdditionalInfo();
        salesAdditionalInfo.setUserAccount(appSession.getCurrentUser());
        salesAdditionalInfo.setCompanyBranch(appSession.getCompanyBranch());
        salesAdditionalInfo.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
        SystemUtils.resetJsfUI();
    }

    public SalesAdditionalInfo getSalesAdditionalInfo() {
        return salesAdditionalInfo;
    }

    public void setSalesAdditionalInfo(SalesAdditionalInfo salesAdditionalInfo) {
        this.salesAdditionalInfo = salesAdditionalInfo;
    }
}
