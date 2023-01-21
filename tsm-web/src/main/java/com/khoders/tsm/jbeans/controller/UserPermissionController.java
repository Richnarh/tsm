/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.entities.system.UserPage;
import com.khoders.tsm.jbeans.dto.PageInfo;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.UserAccountService;
import com.khoders.resource.jpa.CrudApi;
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
@Named
@SessionScoped
public class UserPermissionController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private UserAccountService userAccountService;
    private List<UserPage> userPageList = new LinkedList<>();
    private List<PageInfo> pageInfoList = new LinkedList<>();
    
    @PostConstruct
    private void init(){
        userPageList = userAccountService.userPages(appSession.getCurrentUser());
        
        for (UserPage userPage : userPageList)
        {
            PageInfo pageInfo = new PageInfo();
            if(userPage.getAppPage() != null){
              pageInfo.setPageName(userPage.getAppPage().getPageName());
              pageInfo.setUrl(userPage.getAppPage().getPageUrl());  
            }
            pageInfoList.add(pageInfo);
        }
    }

    public List<PageInfo> getPageInfoList()
    {
        return pageInfoList;
    }
}
