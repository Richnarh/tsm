package com.khoders.tsm.listener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.system.CompanyBranch;
import com.khoders.tsm.entities.system.EventLog;
import com.khoders.tsm.entities.system.EventModule;
import com.khoders.tsm.entities.system.UserAccount;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.logging.Level;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author khoders
 */
@Named(value = "appSession")
@SessionScoped
public class AppSession implements Serializable{
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AppSession.class.getName());
    @Inject private CrudApi crudApi;
    
    private UserAccount currentUser;
    private CompanyBranch companyBranch;
    
    public void login(UserAccount userAccount)
    {
        this.currentUser = userAccount;
    }
    
    public void initBranch(CompanyBranch companyBranch)
    {
        this.companyBranch=companyBranch;
    }
    
    public void logout(){
        this.currentUser = null;
    }

    public UserAccount getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(UserAccount currentUser)
    {
        this.currentUser = currentUser;
    }

    public CompanyBranch getCompanyBranch()
    {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch){
        this.companyBranch = companyBranch;
    }
    
    public EventModule getModule(String moduleName){
        return crudApi.getEm().createQuery("SELECT e FROM EventModule e WHERE e.moduleName = :moduleName", EventModule.class)
                 .setParameter(EventModule._moduleName, moduleName)
                 .getResultStream().findFirst().orElse(null);
    }
    
    public void logEvent(String eventName,String module, String evtIdentifier){
        EventLog evtLog = new EventLog();
        evtLog.setEventName(eventName);
        evtLog.setEventModule(getModule(module));
        evtLog.setEventDate(LocalDateTime.now());
        evtLog.setEventIdentifier(evtIdentifier);
        evtLog.setUserBrowser(SystemUtils.getBrowser());
        evtLog.setUserIpAddress(SystemUtils.getIP());
        evtLog.setCompanyBranch(companyBranch);
        evtLog.setUserAccount(currentUser);
        evtLog.setLastModifiedBy(currentUser != null ? currentUser.getFullname() : null);
        
        crudApi.save(evtLog);
        
        logger.log(Level.INFO, "Event Logged: {0}",eventName);
    }
}
