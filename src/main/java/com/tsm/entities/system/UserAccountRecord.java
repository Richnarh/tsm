/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities.system;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author richa
 */
@MappedSuperclass
public class UserAccountRecord extends RefNo{
    public static final String _userAccount = "userAccount";
    @JoinColumn(name = "user_account", referencedColumnName = "id")
    @ManyToOne
    private UserAccount userAccount;
    
    public static final String _companyBranch = "companyBranch";
    public static final String _companyBranchId = CompanyBranch._id;
    @ManyToOne
    @JoinColumn(name = "company_branch", referencedColumnName = "id")
    private CompanyBranch companyBranch;
    
    @Column(name = "data_source")
    @Lob
    private String dataSource;

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public CompanyBranch getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch) {
        this.companyBranch = companyBranch;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
