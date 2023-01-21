/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.tsm.admin.services.UserAccountService;
import com.khoders.tsm.entities.system.UserAccount;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Faces;

/**
 *
 * @author richa
 */
@Named(value = "userController")
@SessionScoped
public class UserController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private UserAccountService userAccountService;
 
    private List<UserAccount> userAccountList = new LinkedList<>();
    
    @PostConstruct
    private void init()
    {
        userAccountList = userAccountService.getAccountList();
    }

    public List<UserAccount> getUserAccountList()
    {
        return userAccountList;
    }
    
    public void login(UserAccount userAccount)
    {
        
//      Faces.redirect("http://localhost:8080/tsm-web/access.xhtml?id="+userAccount.getId());
        Faces.redirect("http://209.145.49.185:8080/tsm/access.xhtml?id="+userAccount.getId());
    }
}
