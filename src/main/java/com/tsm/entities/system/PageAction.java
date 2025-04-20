/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities.system;

import com.dolphindoors.resource.enums.Status;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "page_action")
public class PageAction extends RefNo
{
    @JoinColumn(name = "app_page", referencedColumnName = "id")
    @ManyToOne
    private AppPage appPage;
    
    @Column(name = "action_code")
    private String actionCode;
    
    @Column(name = "action_name")
    private String actionName;
    
    @Column(name = "action_status")
    @Enumerated(EnumType.STRING)
    private Status actionStatus = Status.ACTIVE;

    public AppPage getAppPage()
    {
        return appPage;
    }

    public void setAppPage(AppPage appPage)
    {
        this.appPage = appPage;
    }
    
    public String getActionCode()
    {
        return actionCode;
    }

    public void setActionCode(String actionCode)
    {
        this.actionCode = actionCode;
    }

    public String getActionName()
    {
        return actionName;
    }

    public void setActionName(String actionName)
    {
        this.actionName = actionName;
    }

    public Status getActionStatus()
    {
        return actionStatus;
    }

    public void setActionStatus(Status actionStatus)
    {
        this.actionStatus = actionStatus;
    }
    
}
