/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities.system;

import com.khoders.resource.enums.Currency;
import java.io.Serializable;
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
@Table(name = "company_profile")
public class CompanyProfile extends RefNo implements Serializable
{
    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.GHS;

    @Column(name = "website")
    private String website;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "tin_no")
    private String tinNo;
        
    public Currency getCurrency()
    {
        return currency;
    }

    public void setCurrency(Currency currency)
    {
        this.currency = currency;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getCompanyEmail()
    {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail)
    {
        this.companyEmail = companyEmail;
    }

    public String getTinNo()
    {
        return tinNo;
    }

    public void setTinNo(String tinNo)
    {
        this.tinNo = tinNo;
    }

    @Override
    public String toString()
    {
        return companyEmail;
    }
}
