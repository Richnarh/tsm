/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.entities.CreditPayment;
import com.khoders.tsm.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.FormView;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.services.SalesService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author khoders
 */
@Named(value = "creditPaymentController")
@SessionScoped
public class CreditPaymentController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private SalesService salesService;
    
    private String optionText;
    
    private CreditPayment creditPayment = new CreditPayment();
    private List<CreditPayment> creditPaymentList = new LinkedList<>();
    private Customer selectedCustomer = null;
   
    private FormView pageView = FormView.listForm();
    
    @PostConstruct
    public void init(){
        clearCreditPayment();
    }
    
    public void initCreditPayment()
    {
        clearCreditPayment();
        pageView.restToCreateView();
    }

   public void saveCreditPayment()
    {
       try{
           creditPayment.genCode();
          if(crudApi.save(creditPayment) != null){
              creditPaymentList = CollectionList.washList(creditPaymentList, creditPayment);
              Msg.info(Msg.SUCCESS_MESSAGE);
              closePage();
          }else{
              Msg.error("Oops! failed to save creditPayment");
          }
           clearCreditPayment();
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
   
   
    public void editCreditPayment(CreditPayment creditPayment)
    {
       pageView.restToCreateView();
       this.creditPayment=creditPayment;
       optionText = "Update";
    }
    
    public void deleteCreditPayment(CreditPayment creditPayment)
    {
        try
        {
          if(crudApi.delete(creditPayment))
          {
              creditPaymentList.remove(creditPayment);
              
              FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.DELETE_MESSAGE, null)); 
          }
          else
          {
              FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, Msg.FAILED_MESSAGE, null));
          }
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void closePage()
    {
       creditPayment = new CreditPayment();
       optionText = "Save Changes";
       pageView.restToListView();
    }
        
    public void clearCreditPayment() {
        creditPayment = new CreditPayment();
        creditPayment.setUserAccount(appSession.getCurrentUser());
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public CreditPayment getCreditPayment()
    {
        return creditPayment;
    }

    public void setCreditPayment(CreditPayment creditPayment)
    {
        this.creditPayment = creditPayment;
    }

    public List<CreditPayment> getCreditPaymentList()
    {
        return creditPaymentList;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
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
