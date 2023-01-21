/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.jbeans.dto;

/**
 *
 * @author richa
 */
public class BaseDto
{
    private String companyAddress;
    private String companyName;
    private String telNumber;
    private String website;

    public String getCompanyAddress()
    {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress)
    {
        this.companyAddress = companyAddress;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getTelNumber()
    {
        return telNumber;
    }

    public void setTelNumber(String telNumber)
    {
        this.telNumber = telNumber;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }
    
}
