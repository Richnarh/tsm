/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.services;

import com.dolphindoors.resource.exception.DataNotFoundException;
import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.entities.system.CompanyBranch;
import com.tsm.entities.system.UserAccount;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pascal
 */
@Stateless
public class AppService {
    @Inject private CrudApi crudApi;
    @Inject private DefaultService ds;
    
    public UserAccount getUser(String userAccountId){
        if(userAccountId == null){
           throw new DataNotFoundException("userAccountId is required");
        }
        UserAccount user =  crudApi.find(UserAccount.class, userAccountId);
        if(user == null){
           throw new DataNotFoundException("User with the ID: "+userAccountId +" cannot be found!");
        }
        return user;
    }
    public CompanyBranch getBranch(String branchId){
        if(branchId == null){
           throw new DataNotFoundException("companyBranchId is required");
        }
        CompanyBranch branch =  crudApi.find(CompanyBranch.class, branchId);
        if(branch == null){
           throw new DataNotFoundException("Company Branch with ID: "+branchId +" cannot be found!");
        }
        return branch;
    }
    public String getBranchName(String branchId){
        if(branchId == null){
           throw new DataNotFoundException("companyBranchId is required");
        }
        CompanyBranch branch =  crudApi.find(CompanyBranch.class, branchId);
        if(branch == null){
           throw new DataNotFoundException("Company Branch with ID: "+branchId +" cannot be found!");
        }
        return branch.getBranchName();
    }
}
