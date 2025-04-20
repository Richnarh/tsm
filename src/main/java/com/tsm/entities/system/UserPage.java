package com.tsm.entities.system;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author richa
 */
@Entity
@Table(name = "user_pages")
public class UserPage extends RefNo
{
    @JoinColumn(name = "app_page")
    @ManyToOne
    private AppPage appPage;
    
    @JoinColumn(name = "user_account")
    @ManyToOne
    private UserAccount userAccount;

    public AppPage getAppPage()
    {
        return appPage;
    }

    public void setAppPage(AppPage appPage)
    {
        this.appPage = appPage;
    }

    public UserAccount getUserAccount()
    {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount)
    {
        this.userAccount = userAccount;
    }
    
    
}
