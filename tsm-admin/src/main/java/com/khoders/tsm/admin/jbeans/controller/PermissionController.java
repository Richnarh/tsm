/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.tsm.admin.listener.AppSession;
import com.khoders.tsm.admin.services.PermissionService;
import com.khoders.tsm.entities.system.AppPage;
import com.khoders.tsm.entities.system.PageAction;
import com.khoders.tsm.entities.system.UserAccount;
import com.khoders.tsm.entities.system.UserPage;
import com.khoders.tsm.entities.system.UserPageAction;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Richard Narh
 */
@Named(value = "permissionController")
@SessionScoped
public class PermissionController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private PermissionService permissionService;
    
    private UserPageAction userPageAction = null;
    private UserPage userPage = null;
    private List<UserPage> userPageList = new LinkedList<>();
    private UserAccount selectedUser = null;
    private List<AppPage> appPageList = new LinkedList<>();
    private List<PageAction> pageActionList = new LinkedList<>();
    private List<UserPageAction> userPageActionList = new LinkedList<>();
    
    @PostConstruct
    private void init(){
        appPageList = permissionService.appPageList();
    }
    
    public void addPageToUser(AppPage appPage)
    {
        System.out.println("Selected User --- "+selectedUser);
        if(selectedUser == null)
        {
            Msg.error("Please select a user");
            return;
        }
        UserPage page = permissionService.userPageExist(selectedUser, appPage);
        if(page != null){
            Msg.error("The "+appPage.getPageName()+" page is already added for "+selectedUser.getFullname());
            return;
        }
        userPage = new UserPage();
        userPage.setAppPage(appPage);
        userPage.genCode();
        userPage.setUserAccount(selectedUser);

        if(crudApi.save(userPage) != null)
        {
           userPageList = CollectionList.washList(userPageList, userPage);
           Msg.info("Page is added to user");
        }
    }
    
    public void selectUser(){
        if(selectedUser == null) return;
        userPageList = permissionService.getUserPageList(selectedUser);
        userPageActionList = permissionService.getUserPageActionList(selectedUser);
    }

    public void activateAction(PageAction pageAction)
    {
        userPageAction.setPageAction(pageAction);
        userPageAction.setLastModifiedBy(appSession.getCurrentUser().getFullname());
        userPageAction.setUserAccount(selectedUser);
        if(crudApi.save(userPageAction) != null){
            userPageActionList = CollectionList.washList(userPageActionList, userPageAction);
            Msg.info("Action set for "+pageAction.getAppPage().getPageName() +"on "+selectedUser.getFullname());
        }
    }
    
    public void setUserPage(UserPage userPage){
         userPageAction = new UserPageAction();
         userPageAction.setUserPage(userPage);
         userPageAction.genCode();
         pageActionList = permissionService.getPageActionList(userPage.getAppPage());
    }
    
    public void editUserPageAction(UserPageAction userPageAction){
        this.userPageAction=userPageAction;
    }
    
    public void deleteUserPageAction(UserPageAction userPageAction){
        try
        {
            if (crudApi.delete(userPageAction))
            {
                userPageActionList.remove(userPageAction);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
        } catch (Exception e)
        {
           e.printStackTrace();
        }
    }
    public void deleteUserPage(UserPage userPage){
        try
        {
            if (crudApi.delete(userPage))
            {
                userPageList.remove(userPage);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
        } catch (Exception e)
        {
           e.printStackTrace();
        }
    }

    public UserAccount getSelectedUser()
    {
        return selectedUser;
    }

    public void setSelectedUser(UserAccount selectedUser)
    {
        this.selectedUser = selectedUser;
    }

    public List<AppPage> getAppPageList()
    {
        return appPageList;
    }

    public List<UserPage> getUserPageList()
    {
        return userPageList;
    }

    public List<PageAction> getPageActionList()
    {
        return pageActionList;
    }

    public List<UserPageAction> getUserPageActionList()
    {
        return userPageActionList;
    }
    
}
