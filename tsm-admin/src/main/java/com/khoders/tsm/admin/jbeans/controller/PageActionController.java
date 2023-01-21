/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.tsm.admin.services.PermissionService;
import com.khoders.tsm.entities.system.AppPage;
import com.khoders.tsm.entities.system.PageAction;
import com.khoders.resource.enums.Status;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Named(value = "pageActionController")
@SessionScoped
public class PageActionController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private PermissionService permissionService;
    private PageAction pageAction = new PageAction();
    private List<PageAction> pageActionList = new LinkedList<>();
    private List<AppPage> appPageList = new LinkedList<>();
    
    private String optionText;
    
    @PostConstruct
    private void init(){
        clearPage();
        pageActionList = permissionService.getPageActionList();
    }
    
    public void initDefault()
    {
        appPageList = permissionService.getAppPageList();
        for (AppPage appPage : appPageList)
        {
            initActions(appPage, "create", "Create", Status.ACTIVE);
            initActions(appPage, "edit", "Edit", Status.ACTIVE);
            initActions(appPage, "delete", "Delete", Status.ACTIVE);
        }
        init();
    }

    private PageAction initActions(AppPage appPage, String actionCode, String actionName, Status actionStatus)
    {
        PageAction action = new PageAction();
        action.setActionCode(actionCode);
        action.setActionName(actionName);
        action.setActionStatus(actionStatus);
        action.setAppPage(appPage);
        action.genCode();
        action.setLastModifiedDate(LocalDateTime.now());
        
        crudApi.save(action);

        return action;
    }      
    
    public void savePageAction()
    {
        try
        {
            if(crudApi.save(pageAction) != null)
            {
              pageActionList = CollectionList.washList(pageActionList, pageAction);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
            clearPage();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void editPageAction(PageAction pageAction){
        this.pageAction=pageAction;
        optionText = "Update";
    }
    
    public void deletePageAction(PageAction pageAction){
        try
        {
            if(crudApi.delete(pageAction)){
                pageActionList.remove(pageAction);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clearPage(){
        pageAction = new PageAction();
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public PageAction getPageAction()
    {
        return pageAction;
    }

    public void setPageAction(PageAction pageAction)
    {
        this.pageAction = pageAction;
    }

    public List<PageAction> getPageActionList()
    {
        return pageActionList;
    }

    public String getOptionText()
    {
        return optionText;
    }


    
    
}
