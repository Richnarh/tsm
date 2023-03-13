/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities.system;

import com.khoders.tsm.enums.EventType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "event_module")
public class EventModule extends RefNo{
    public static final String _moduleName = "moduleName";
    @Column(name = "module_name")
    private String moduleName;
    
    public static final String _eventType = "eventType";
    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    
    @Column(name = "description")
    @Lob
    private String description;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
