/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities.system;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "company_branch")
public class CompanyBranch extends RefNo implements Serializable
{    
    @Column(name = "branch_name")
    private String branchName;
    
    @Column(name = "box_address")
    private String boxAddress;

    @Column(name = "gps_address")
    private String gpsAddress;

    @Column(name = "branch_address")
    private String branchAddress;

    @Column(name = "telephone_no")
    private String telephoneNo;
    
    @JoinColumn(name = "company_profile", referencedColumnName = "id")
    @ManyToOne
    private CompanyProfile companyProfile;
    
    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    public String getBoxAddress()
    {
        return boxAddress;
    }

    public void setBoxAddress(String boxAddress)
    {
        this.boxAddress = boxAddress;
    }

    public String getGpsAddress()
    {
        return gpsAddress;
    }

    public void setGpsAddress(String gpsAddress)
    {
        this.gpsAddress = gpsAddress;
    }

    public String getTelephoneNo()
    {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo)
    {
        this.telephoneNo = telephoneNo;
    }

    public CompanyProfile getCompanyProfile()
    {
        return companyProfile;
    }

    public void setCompanyProfile(CompanyProfile companyProfile)
    {
        this.companyProfile = companyProfile;
    }

    public String getBranchAddress()
    {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress)
    {
        this.branchAddress = branchAddress;
    }
    
    
    @Override
    public String toString()
    {
       return branchName;
    }

}
