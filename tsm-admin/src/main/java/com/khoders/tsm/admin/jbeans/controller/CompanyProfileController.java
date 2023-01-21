/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.admin.services.CompanyService;
import com.khoders.tsm.entities.system.CompanyProfile;
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
 * @author richa
 */
@Named(value = "companyProfileController")
@SessionScoped
public class CompanyProfileController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private CompanyService companyService;
    private List<CompanyProfile> companyProfileList = new LinkedList<>();
    private CompanyProfile companyProfile = new CompanyProfile();
    private String optionText;
    
    @PostConstruct
    private void init()
    {
        optionText="Save Changes";
        companyProfileList = companyService.getCompanyProfileList();
    }
    
    public void saveCompanyProfile()
    {
        try
        {
            if(crudApi.save(companyProfile) != null)
            {
                companyProfileList = CollectionList.washList(companyProfileList, companyProfile);
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.SUCCESS_MESSAGE, null));
            }
            clearProfile();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void editCompanyProfile(CompanyProfile companyProfile)
    {
        this.companyProfile = companyProfile;
        optionText = "Update";
    }
    
    public void deleteCompanyProfile(CompanyProfile companyProfile)
    {
        try
        {
            if(crudApi.delete(companyProfile))
            {
                companyProfileList.remove(companyProfile);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clearProfile()
    {
        companyProfile = new CompanyProfile();
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }
    
    public CompanyProfile getCompanyProfile()
    {
        return companyProfile;
    }

    public void setCompanyProfile(CompanyProfile companyProfile)
    {
        this.companyProfile = companyProfile;
    }

    public List<CompanyProfile> getCompanyProfileList()
    {
        return companyProfileList;
    }

    public String getOptionText()
    {
        return optionText;
    }

    public void setOptionText(String optionText)
    {
        this.optionText = optionText;
    }
    
}
