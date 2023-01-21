package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import static com.khoders.resource.utilities.SecurityUtil.hashText;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.admin.services.UserAccountService;
import com.khoders.tsm.entities.system.UserAccount;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author 
 */
@Named(value = "userAccountController")
@SessionScoped
public class UserAccountController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private UserAccountService userAccountService;
    
    private UserAccount userAccount = new UserAccount();
    private List<UserAccount> userAccountList = new LinkedList<>();
    private List<UserAccount> userPermissionsList = new LinkedList<>();
    private UserAccount selectedAccount=null;
    
    private String optionText;
    
    @PostConstruct
    private void init()
    {
        optionText = "Save Changes";
        userAccountList = userAccountService.getAccountList();
    }
    
    public void selectedAccountActn(UserAccount userAccount)
    {
        userPermissionsList = userAccountService.getUserPermissionsList(userAccount);
        selectedAccount = userAccount;
    }
    
    public void checkAll()
    {
        if(selectedAccount == null){
           Msg.error("Please select a user");
             return;
        }
        
//       selectedAccount.setPermSave(true);
//       selectedAccount.setPermUpdate(true);
//       selectedAccount.setPermDelete(true);
//       selectedAccount.setPermPrint(true);
//       selectedAccount.setPermConvert(true);
//       selectedAccount.setPermSendSMS(true);
    }
    public void savePermissions()
    {
        if(selectedAccount == null)
        {
          Msg.info("Please select a user");
          return;
        }
        
        try
        {
           if(crudApi.save(selectedAccount)!= null)
           {
            userPermissionsList = CollectionList.washList(userPermissionsList, selectedAccount);
            Msg.info("User permissions saved!");
           }
            clear();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void editPermissions(UserAccount selectedAccount)
    {
        this.selectedAccount = selectedAccount;
        optionText = "Update";
    }
    
    public void deletePermissions(UserAccount selectedAccount)
    {
        try
        {
            if(crudApi.delete(selectedAccount))
            {
                userPermissionsList.remove(selectedAccount);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void saveUserAccount()
    {
        try
        {
            userAccount.setEmailAddress(userAccount.getEmailAddress());
            userAccount.setPassword(userAccount.getPassword());
            if (crudApi.save(userAccount) != null)
            {
             userAccountList = CollectionList.washList(userAccountList, userAccount);
             Msg.info("User saved!");
            }
            clear();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
     
    public void editUserAccount(UserAccount userAccount)
    {
        this.userAccount = userAccount;
        optionText = "Update";
    }
    
    public void deleteUserAccount(UserAccount userAccount)
    {
        try
        {
            if(crudApi.delete(userAccount))
            {
              Msg.info("User deleted!");
              userAccountList.remove(userAccount);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void updatePassword()
    {
        if(userAccount.getEmailAddress() == null)
        {
          Msg.error("Please select a user");
          return;
        }
        try
        {
            UserAccount account = crudApi.find(UserAccount.class, userAccount.getId());
            account.setPassword(hashText(userAccount.getPassword()));
            if(crudApi.save(account) != null)
            {
              Msg.info(userAccount.getFullname()+"'s password is updated!");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clear()
    {
        userAccount = new UserAccount();
        selectedAccount = null;
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public UserAccount getUserAccount()
    {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount)
    {
        this.userAccount = userAccount;
    }

    public List<UserAccount> getUserAccountList()
    {
        return userAccountList;
    }

    public String getOptionText()
    {
        return optionText;
    }

    public void setOptionText(String optionText)
    {
        this.optionText = optionText;
    }

    public UserAccount getSelectedAccount()
    {
        return selectedAccount;
    }

    public void setSelectedAccount(UserAccount selectedAccount)
    {
        this.selectedAccount = selectedAccount;
    }

    public List<UserAccount> getUserPermissionsList()
    {
        return userPermissionsList;
    }
    
    
}
