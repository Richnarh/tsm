/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.services;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Stringz;
import com.khoders.tsm.entities.system.CompanyBranch;
import com.khoders.tsm.entities.system.CompanyProfile;
import com.khoders.tsm.entities.system.Permission;
import com.khoders.tsm.entities.system.UserAccount;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author richa
 */
@Stateless
public class CompanyService
{
    @Inject private CrudApi crudApi;
    
   public List<CompanyProfile> getCompanyProfileList()
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM CompanyProfile e", CompanyProfile.class).getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
   
   public List<Permission> getPermissions(UserAccount userAccount)
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM Permission e WHERE e.userAccount=?1", Permission.class)
                    .setParameter(1, userAccount)
                    .getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<CompanyBranch> getCompanyBranchList()
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM CompanyBranch e ORDER BY e.branchName ASC", CompanyBranch.class).getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    public List<UserAccount> getUserAccountList()
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM UserAccount e", UserAccount.class).getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<String> getTables() {
        List<String> tableList = crudApi.getEm().createNativeQuery("SELECT table_name FROM information_schema.tables WHERE table_type='BASE TABLE' AND table_schema = 'tsm' ORDER BY table_name ASC")
                .getResultList();
        
         tableList.stream().map(data ->{
           String[] str = data.split("_");
           switch (str.length) {
               case 2:
                   data = Stringz.capitalizeOnlyFirst(str[0]) + " "+Stringz.capitalizeOnlyFirst(str[1]);
                   break;
               case 3:
                   data = Stringz.capitalizeOnlyFirst(str[0]) + " "+Stringz.capitalizeOnlyFirst(str[1]) +" "+Stringz.capitalizeOnlyFirst(str[2]);
                   break;
               default:
                   data = Stringz.capitalizeOnlyFirst(str[0]);
                   break;
           }
           return data;
       }).collect(Collectors.toList());
         
       return tableList;
    }
}
