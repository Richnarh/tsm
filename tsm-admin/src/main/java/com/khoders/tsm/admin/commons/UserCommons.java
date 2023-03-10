/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.commons;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Stringz;
import com.khoders.tsm.admin.services.CompanyService;
import com.khoders.tsm.admin.services.PermissionService;
import com.khoders.tsm.entities.system.CompanyBranch;
import com.khoders.tsm.entities.system.CompanyProfile;
import com.khoders.tsm.entities.system.AppPage;
import com.khoders.tsm.entities.system.UserAccount;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "userCommons")
@SessionScoped
public class UserCommons implements Serializable
{
   @Inject private CrudApi crudApi;
   @Inject private CompanyService companyService;
   @Inject private PermissionService permissionService;
   
   private List<CompanyBranch> companyBranchList = new LinkedList<>();
   private List<CompanyProfile> companyProfileList = new LinkedList<>();
   private List<UserAccount> userAccountList = new LinkedList<>();
   private List<AppPage> appPageList = new LinkedList<>();
   private List<String> tableList = new LinkedList<>();
   
   @PostConstruct
   public void init()
   {
       companyBranchList = companyService.getCompanyBranchList();
       companyProfileList = companyService.getCompanyProfileList();
       userAccountList = companyService.getUserAccountList();
       appPageList = permissionService.getAppPageList();
       tableList = companyService.getTables();
   }
   
    public List<CompanyBranch> getCompanyBranchList()
    {
        return companyBranchList;
    }

    public List<CompanyProfile> getCompanyProfileList()
    {
        return companyProfileList;
    }

    public List<UserAccount> getUserAccountList()
    {
        return userAccountList;
    }

    public List<AppPage> getAppPageList()
    {
        return appPageList;
    }

    public List<String> getTableList() {
        return tableList;
    }
    
}
