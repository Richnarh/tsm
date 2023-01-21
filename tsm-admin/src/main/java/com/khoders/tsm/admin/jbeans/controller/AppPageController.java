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
import com.khoders.tsm.admin.services.PermissionService;
import com.khoders.tsm.entities.system.AppPage;
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
@Named(value = "appPageController")
@SessionScoped
public class AppPageController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private PermissionService permissionService;
    private List<AppPage> appPageList = new LinkedList<>();
    private AppPage appPage = new AppPage();
    private String optionText;
    
    @PostConstruct
    private void init()
    {
        optionText="Save Changes";
        appPageList = permissionService.getAppPageList();
    }
    
    public void savePage()
    {
        try
        {
            appPage.genCode();
            if(crudApi.save(appPage) != null)
            {
                appPageList = CollectionList.washList(appPageList, appPage);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
            clearPage();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void editPage(AppPage appPage)
    {
        this.appPage = appPage;
        optionText = "Update";
        SystemUtils.resetJsfUI();
    }
    
    public void deletePage(AppPage appPage)
    {
        try
        {
            if(crudApi.delete(appPage))
            {
                appPageList.remove(appPage);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clearPage()
    {
        appPage = new AppPage();
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public AppPage getAppPage()
    {
        return appPage;
    }

    public void setAppPage(AppPage appPage)
    {
        this.appPage = appPage;
    }

    public List<AppPage> getAppPageList()
    {
        return appPageList;
    }
    
    public List<AppPage> getPageList()
    {
        return appPageList;
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
