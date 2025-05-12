/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.tsm.entities.system.CompanyRecord;
import com.tsm.enums.EventModule;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "event_log")
public class EventLog extends CompanyRecord implements Serializable{
    public static final String _eventName = "eventName";
    @Column(name = "event_name")
    private String eventName;
    
    public static final String _eventIdentifier = "eventIdentifier";
    @Column(name = "event_identifier")
    private String eventIdentifier;
    
    public static final String _eventDate = "eventDate";
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    
    public static final String _userBrowser = "userBrowser";
    @Column(name = "user_browser")
    private String userBrowser;
    
    public static final String _userIpAddress = "userIpAddress";
    @Column(name = "user_ip_address")
    private String userIpAddress;
    
    public static final String _eventModule = "eventModule";
    @Column(name = "event_module")
    @Enumerated(EnumType.STRING)
    private EventModule eventModule;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventIdentifier() {
        return eventIdentifier;
    }

    public void setEventIdentifier(String eventIdentifier) {
        this.eventIdentifier = eventIdentifier;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getUserBrowser() {
        return userBrowser;
    }

    public void setUserBrowser(String userBrowser) {
        this.userBrowser = userBrowser;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public void setUserIpAddress(String userIpAddress) {
        this.userIpAddress = userIpAddress;
    }

    public EventModule getEventModule() {
        return eventModule;
    }

    public void setEventModule(EventModule eventModule) {
        this.eventModule = eventModule;
    }
    
}
