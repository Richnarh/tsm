package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.Pages;
import com.khoders.tsm.entities.system.UserAccount;
import com.khoders.tsm.jbeans.UserModel;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.UserAccountService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Msg;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Faces;

/**
 *
 * @author richard
 */
@Named(value="loginController")
@RequestScoped
public class LoginController implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private UserAccountService userAccountService;

    private String emailAddress;
    private String password;
    private UserModel userModel = new UserModel();
        
    public String doLogin(){
        try
        {
            userModel.setEmailAddress(emailAddress);
            userModel.setPassword(password);

            UserAccount account = userAccountService.login(userModel);

            if (account == null){
               Msg.error("Wrong username or Password");
               return null;
            }

            initLogin(account);
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    public String initLogin(UserAccount userAccount)
    {
        try
        {
            if (userAccount == null)
            {
             Msg.error("Wrong username or Password");
                return null;
            }
            appSession.login(userAccount);
            appSession.initBranch(userAccount.getCompanyBranch());
            Faces.redirect(Pages.index);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String doLogout()
    {
        try
        {
            Faces.invalidateSession();
            Faces.logout();

            Faces.redirect(Pages.login);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    
}
