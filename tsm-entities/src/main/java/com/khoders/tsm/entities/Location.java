/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.entities;

import com.khoders.tsm.entities.system.UserAccountRecord;
import com.khoders.tsm.enums.LocType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "location")
public class Location  extends UserAccountRecord implements Serializable{
    public static final String _locationName = "locationName";
    @Column(name = "location_name")
    private String locationName;
    
    @Column(name = "address")
    private String address;
    
    public static final String _locType = "locType";
    @Column(name = "loc_type")
    @Enumerated(EnumType.STRING)
    private LocType locType;
    
    public static final String _mainWarehouse = "mainWarehouse";
    @Column(name = "main_warehouse")
    private boolean mainWarehouse;
    
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocType getLocType() {
        return locType;
    }

    public void setLocType(LocType locType) {
        this.locType = locType;
    }

    public boolean isMainWarehouse() {
        return mainWarehouse;
    }

    public void setMainWarehouse(boolean mainWarehouse) {
        this.mainWarehouse = mainWarehouse;
    }

    @Override
    public String toString() {
        return locationName;
    }
}
