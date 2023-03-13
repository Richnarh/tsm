/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import com.khoders.tsm.admin.services.CompanyService;
import com.khoders.tsm.entities.system.EventModule;
import com.khoders.tsm.enums.EventType;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Pascal
 */
@Named(value = "eventModuleController")
@SessionScoped
public class EventModuleController implements Serializable{
    @Inject private CrudApi crudApi;
    @Inject private CompanyService companyService;
    
    private EventModule eventModule = new EventModule();
    private List<EventModule> eventModuleList = new LinkedList<>();
    
    @PostConstruct
    private void init(){
        eventModuleList = companyService.getEventModules();
    }
    
    public void saveEventModule(){
        try {
            if(crudApi.save(eventModule) != null){
                eventModuleList = CollectionList.washList(eventModuleList, eventModule);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
            clearEvent();
        } catch (Exception e) {
        }
    }
    
    public void editEventModule(EventModule eventModule){
        this.eventModule=eventModule;
    }
    
    public void deleteEventModule(EventModule eventModule){
        try {
            if(crudApi.delete(eventModule)){
                eventModuleList.remove(eventModule);
                Msg.info(Msg.SUCCESS_MESSAGE);
            }
        } catch (Exception e) {
        }
    }
    
    public void initDefault() {
        List<String> tableList = companyService.getTables();        
        tableList.forEach(this::initModule);
        
        init();
    }
    
    private EventModule initModule(String module){
        List<EventType> eventTypeList = Arrays.asList(EventType.values());     
        EventModule evtModule = null;
        for (EventType eventType : eventTypeList) {
            if(companyService.moduleExist(module, eventType)){
                break;
            }
            evtModule = new EventModule();
            evtModule.setEventType(eventType);
            evtModule.setModuleName(module);
            evtModule.setDescription("Initialised");
            crudApi.save(evtModule);
        }
        return evtModule;
    }
    
    public void clearEvent(){
        eventModule = new EventModule();
    }

    public EventModule getEventModule() {
        return eventModule;
    }

    public void setEventModule(EventModule eventModule) {
        this.eventModule = eventModule;
    }

    public List<EventModule> getEventModuleList() {
        return eventModuleList;
    }
}
