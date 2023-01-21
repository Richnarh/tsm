/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.admin.services.CompanyService;
import com.khoders.tsm.entities.system.CompanyBranch;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "companyBranchController")
@SessionScoped
public class CompanyBranchController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private CompanyService companyService;
    
    private List<CompanyBranch> companyBranchList = new LinkedList<>();
    private CompanyBranch companyBranch = new CompanyBranch();
    
    private String optionText;
    
    @PostConstruct
    private void init()
    {
        optionText = "Save Changes";
        companyBranchList = companyService.getCompanyBranchList();
    }
    
    public void saveCompanyBranch()
    {
        try
        {
            if(crudApi.save(companyBranch) != null)
            {
                companyBranchList = CollectionList.washList(companyBranchList, companyBranch);
            }
            clearBranch();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void editCompanyBranch(CompanyBranch companyBranch)
    {
        this.companyBranch = companyBranch;
        optionText = "Update";
    }
    
    public void deleteCompanyBranch(CompanyBranch companyBranch)
    {
        try
        {
            if(crudApi.delete(companyBranch))
            {
                companyBranchList.remove(companyBranch);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clearBranch()
    {
        companyBranch = new CompanyBranch();
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }
    
    

    public CompanyBranch getCompanyBranch()
    {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch)
    {
        this.companyBranch = companyBranch;
    }

    public List<CompanyBranch> getCompanyBranchList()
    {
        return companyBranchList;
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
