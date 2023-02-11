/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.Tax;
import com.khoders.tsm.services.SalesService;
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
@Named(value = "taxController")
@SessionScoped
public class TaxController implements Serializable{
    @Inject CrudApi crudApi;
    @Inject SalesService salesService;
    
    private String optionText;
    
    private Tax tax = new Tax();
    private List<Tax> taxList = new LinkedList<>();
    
    @PostConstruct
    private void init()
    {
        clearTax();
        
        taxList = salesService.getTaxList();
    }
    
   public void saveTax()
    {
        try 
        {
          if(crudApi.save(tax) != null)
          {
              taxList = CollectionList.washList(taxList, tax);
              
              Msg.info(Msg.SUCCESS_MESSAGE);
          }
          else
          {
            Msg.error("Oops! failed to create tax");
          }
           clearTax();
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
   
    public void editTax(Tax tax)
    {
       optionText = "Update";
       this.tax=tax;
    }
    
    public void deleteTax(Tax tax)
    {
        try
        {
          if(crudApi.delete(tax))
          {
              taxList.remove(tax);
              
             Msg.info(Msg.DELETE_MESSAGE);
          }
          else
          {
              Msg.error(Msg.FAILED_MESSAGE);
          }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void clearTax() {
        tax = new Tax();
        tax.genCode();
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Tax getTax()
    {
        return tax;
    }

    public void setTax(Tax tax)
    {
        this.tax = tax;
    }

    public List<Tax> getTaxList()
    {
        return taxList;
    }
    
}
