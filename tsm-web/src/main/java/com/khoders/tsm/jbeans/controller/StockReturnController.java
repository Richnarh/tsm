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
import com.khoders.tsm.entities.StockReturn;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.InventoryService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

    private StockReturn stockReturn = new StockReturn();
    private List<StockReturn> stockReturnList = new LinkedList<>();

    private FormView pageView = FormView.listForm();
    private String optionText;

    @PostConstruct
    private void init()
    {
        clearStockReturn();
        stockReturnList = inventoryService.getStockReturnList();
    }

    public void initCLient()
    {
        clearStockReturn();
        pageView.restToCreateView();
    }
    
    public void updateInventory(StockReturn stockReturn)
    {

    }

    public void saveStockReturn()
    {
        try
        {
            stockReturn.setUserAccount(appSession.getCurrentUser());
            stockReturn.setCompanyBranch(appSession.getCompanyBranch());
            if (crudApi.save(stockReturn) != null)
            {
                stockReturnList = CollectionList.washList(stockReturnList, stockReturn);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.setMsg("Return good saved!"), null));
            } else
            {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.FAILED_MESSAGE, null));
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

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.SUCCESS_MESSAGE, null));
            } else
            {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.FAILED_MESSAGE, null));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void editStockReturn(StockReturn stockReturn)
    {
        pageView.restToCreateView();
        this.stockReturn = stockReturn;
        optionText = "Update";
    }

    public void clearStockReturn()
    {
        stockReturn = new StockReturn();
        stockReturn.setUserAccount(appSession.getCurrentUser());
        stockReturn.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public void closePage()
    {
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

}
