/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.jbeans.controller;

import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.FormView;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.entities.Customer;
import com.tsm.services.InventoryService;
import com.tsm.listener.AppSession;
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
@Named(value = "customerController")
@SessionScoped
public class CustomerController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private InventoryService inventoryService;

    private Customer customer = new Customer();
    private List<Customer> customerList = new LinkedList<>();

    private FormView pageView = FormView.listForm();
    private String optionText;

    @PostConstruct
    public void init()
    {
        clearCustomer();
        customerList = inventoryService.getCustomerList();
    }

    public void initCLient()
    {
        clearCustomer();
        pageView.restToCreateView();
    }

    public void saveCustomer()
    {
        if (!optionText.equals("Update"))
        {
            if (customer != null)
            {
                Customer object = inventoryService.customertExist(customer.getPhone());

                if (object != null)
                {
                   Msg.error("The customer with phone number: " + customer.getPhone() + " already exist");
                   return;
                }
            }
        }
        try
        {
            customer.genCode();
            if (crudApi.save(customer) != null)
            {
                customerList = JUtils.addToList(customerList, customer);
                Msg.info(Msg.SUCCESS_MESSAGE);
            } else
            {
              Msg.error(Msg.FAILED_MESSAGE);
            }
            clearCustomer();
            closePage();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteCustomer(Customer customer)
    {
        try
        {
            if (crudApi.delete(customer))
            {
                customerList.remove(customer);
                Msg.info(Msg.SUCCESS_MESSAGE);
            } else
            {
               Msg.error(Msg.FAILED_MESSAGE);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void editCustomer(Customer customer)
    {
        pageView.restToCreateView();
        this.customer = customer;
        optionText = "Update";
    }

    public void clearCustomer()
    {
        customer = new Customer();
        customer.setUserAccount(appSession.getCurrentUser());
        customer.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        JUtils.resetJsfUI();
    }

    public void closePage()
    {
        customer = new Customer();
        customer.setUserAccount(appSession.getCurrentUser());
        customer.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        pageView.restToListView();
    }

    public List<Customer> getCustomerList()
    {
        return customerList;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer bird)
    {
        this.customer = bird;
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
