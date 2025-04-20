/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.jbeans.controller;

import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.Pages;
import com.tsm.entities.system.UserAccount;
import com.tsm.listener.AppSession;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Faces;

/**
 *
 * @author richa
 */
@Named(value="backdoorController")
@ViewScoped
public class backdoorController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    private UserAccount userAccount;
    
    
    private String userId;
    
    public void user(String id)
    {
        userId = id;
        System.out.println("UserID => "+userId);
        System.out.println("ID => "+id);
    }
    
    public String backdoorAccess()
    {
        try
        {
            UserAccount thisAccount = crudApi.find(UserAccount.class, userId);
            System.out.println("ID -- " + userId);
            if (thisAccount == null)
            {
                return null;
            }
           initAccountLogin(thisAccount);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    
    public String initAccountLogin(UserAccount userAccount)
    {
        try
        {
            if (userAccount == null)
            {
               Msg.error("Wrong username or Password");
               return null;
            }
            appSession.login(userAccount);
            appSession.initBranch(userAccount.getCompanyBranch());
            
            Faces.redirect(Pages.index);
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

        
}
