/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SecurityUtil;
import com.khoders.tsm.admin.listener.AppSession;
import com.khoders.tsm.entities.system.UserAccount;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author khoders
 */
@Named(value = "profileUpdateController")
@SessionScoped
public class ProfileUpdateController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    
    private String password;
    private String confirmPassword;
    private UserAccount userAccount;
    
    public void updateAccount()
    {
        try 
        {
            if(appSession.getCurrentUser() != null)
            {
                crudApi.save(appSession.getCurrentUser());
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
            else
            {
             Msg.info(Msg.DELETE_MESSAGE);  
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
           
    public void updatePassword()
    {
        if(userAccount == null){
            Msg.error("Please select a user");
            return;
        }
        if(!password.equals(confirmPassword))
        {
            Msg.info("Password do not match");
            return;
        }
     
        userAccount.setPassword(SecurityUtil.hashText(password));
        if(crudApi.save(userAccount) != null)
        {
          Msg.info("Password Reset successful!");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }    
}
