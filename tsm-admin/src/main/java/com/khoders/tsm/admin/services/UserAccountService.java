/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.services;

import com.khoders.resource.jpa.CrudApi;
import static com.khoders.resource.utilities.SecurityUtil.hashText;
import com.khoders.tsm.admin.UserModel;
import com.khoders.tsm.entities.system.UserAccount;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

/**
 *
 * @author Khoders
 */
@Stateless
public class UserAccountService
{
    @Inject private CrudApi crudApi;
    
    public UserAccount login(UserModel userModel)
    {
        
        try
        {
            String qryString = "SELECT e FROM UserAccount e WHERE e.emailAddress=?1 AND e.password=?2";
            TypedQuery<UserAccount> typedQuery = crudApi.getEm().createQuery(qryString, UserAccount.class)
                    .setParameter(1, userModel.getUserEmail())
                    .setParameter(2, hashText(userModel.getPassword()));
            
                   return typedQuery.getResultStream().findFirst().orElse(null);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    public boolean isTaken(String username)
    {
        String qryString = "SELECT e FROM UserAccount e WHERE e.email_address=?1";
        try {
            UserAccount account = crudApi.getEm().createQuery(qryString, UserAccount.class)
                    .setParameter(1, username)
                    .getSingleResult();
            
            return account != null;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<UserAccount> getAccountList()
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

    public List<UserAccount> getUserPermissionsList(UserAccount userAccount)
    {
        try
        {
            String qryString = "SELECT e FROM UserAccount e WHERE e.username=?1";
            return crudApi.getEm().createQuery(qryString, UserAccount.class).setParameter(1, userAccount.getEmailAddress()).getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
