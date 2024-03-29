/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.config;

import com.khoders.tsm.entities.system.AppPage;
import com.khoders.tsm.entities.system.UserAccount;
import com.khoders.tsm.entities.system.UserPage;
import com.khoders.tsm.jbeans.UserModel;
import com.khoders.tsm.entities.Customer;
import com.khoders.resource.jpa.CrudApi;
import static com.khoders.resource.utilities.SecurityUtil.hashText;
import com.khoders.tsm.enums.CustomerType;
import com.khoders.tsm.services.UserAccountService;
import com.khoders.resource.enums.AccessLevel;
import com.khoders.resource.enums.ClientType;
import com.khoders.tsm.services.SalesService;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author richa
 */
@Singleton
@Startup
public class AppInit
{

    @Inject private CrudApi crudApi;
    @Inject private UserAccountService userAccountService;
    @Inject private SalesService salesService;
    private List<AppPage> appPageList = new LinkedList<>();
    private UserModel userModel = new UserModel();

    @PostConstruct
    public void init(){
        System.out.println("******************************************");
        System.out.println("******************************************");

        System.out.println("application started at - " + LocalDateTime.now());
        System.out.println("****  Going to create default data *******");

        System.out.println("******************************************");
        System.out.println("******************************************");

        createUser();
        initCustomer();
    }
    
    public void createUser(){
        try{
            String defaultUser = "tsm";
            userModel.setEmailAddress(defaultUser);
            userModel.setPassword(defaultUser);
            UserAccount userAccount = userAccountService.login(userModel);

            if (userAccount != null){
                return;
            }

            userAccount = new UserAccount();
            userAccount.setAccessLevel(AccessLevel.SUPER_USER);
            userAccount.setEmailAddress(defaultUser);
            userAccount.setFullname(defaultUser);
            userAccount.setPassword(hashText(defaultUser));

            crudApi.save(userAccount);
            initPermission(userAccount);
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void initPermission(UserAccount userAccount){
        appPageList = userAccountService.getAppPageList();
        
        appPageList.forEach(appPage ->
        {
            initDefaultPerm(appPage, userAccount);
        });
    }
    
    private UserPage initDefaultPerm(AppPage appPage, UserAccount userAccount){
        UserPage userPage = new UserPage();
        userPage.setUserAccount(userAccount);
        userPage.setAppPage(appPage);
        userPage.genCode();
        userPage.setLastModifiedBy(userAccount.getEmailAddress());
        userPage.setLastModifiedDate(LocalDateTime.now());
        crudApi.save(userPage);
        
        return userPage;
    }
    
    public void initCustomer()
    {
        Customer c = salesService.defaultCustomer(CustomerType.WALK_IN_CUSTOMER);
        if(c != null)return;
        
        Customer customer = new Customer();
        customer.setClientType(ClientType.CUSTOMER);
        customer.setCustomerName(CustomerType.WALK_IN_CUSTOMER.getLabel());
        customer.setPhone("");
        
        crudApi.save(customer);

        Customer back = salesService.defaultCustomer(CustomerType.BACK_LOG_SUPPLIER);
        if(back != null)return;
        
        Customer cc = new Customer();
        cc.setClientType(ClientType.CUSTOMER);
        cc.setCustomerName(CustomerType.BACK_LOG_SUPPLIER.getLabel());
        cc.setPhone("");
        
        crudApi.save(cc);
    }
}
