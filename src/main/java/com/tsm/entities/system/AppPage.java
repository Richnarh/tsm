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
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "app_page")
public class AppPage extends RefNo
{
    @Column(name = "page_code")
    private String pageCode;
    
    @Column(name = "page_name")
    private String pageName;
    
    @Column(name = "page_url")
    private String pageUrl;
    
    @Column(name = "reorder")
    private Integer reorder;
    
    @Column(name = "page_status")
    @Enumerated(EnumType.STRING)
    private Status pageStatus = Status.ACTIVE;

    public String getPageName()
    {
        return pageName;
    }

    public void setPageName(String pageName)
    {
        this.pageName = pageName;
    }

    public String getPageUrl()
    {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl)
    {
        this.pageUrl = pageUrl;
    }

    public String getPageCode()
    {
        return pageCode;
    }

    public void setPageCode(String pageCode)
    {
        this.pageCode = pageCode;
    }

    public Status getPageStatus()
    {
        return pageStatus;
    }

    public void setPageStatus(Status pageStatus)
    {
        this.pageStatus = pageStatus;
    }

    public Integer getReorder()
    {
        return reorder;
    }

    public void setReorder(Integer reorder)
    {
        this.reorder = reorder;
    }
    
    @Override
    public String toString()
    {
        return pageName;
    }
    
}
