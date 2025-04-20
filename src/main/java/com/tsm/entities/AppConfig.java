/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.entities;

import com.dolphindoors.resource.enums.Status;
import com.dolphindoors.resource.jpa.BaseModel;
import java.io.Serializable;
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
@Table(name = "app_config")
public class AppConfig extends BaseModel implements Serializable{
    public static final String _configName = "configName";
    @Column(name = "config_name")
    private String configName;
    
    public static final String _configValue = "configValue";
    @Column(name = "config_value")
    private String configValue;
    
    public static final String _configStatus = "configStatus";
    @Column(name = "config_status")
    @Enumerated(EnumType.STRING)
    private Status configStatus;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Status getConfigStatus() {
        return configStatus;
    }

    public void setConfigStatus(Status configStatus) {
        this.configStatus = configStatus;
    }
}
