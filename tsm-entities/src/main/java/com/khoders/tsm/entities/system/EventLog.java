/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities.system;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "event_log")
public class EventLog extends UserAccountRecord implements Serializable{
    @Column(name = "event_name")
    private String eventName;
    
    @Column(name = "event_identifier")
    private String eventIdentifier;
    
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    
    @Column(name = "user_browser")
    private String userBrowser;
    
    @Column(name = "user_ip_address")
    private String userIpAddress;
    
    @JoinColumn(name = "event_module", referencedColumnName = "id")
    @ManyToOne
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
