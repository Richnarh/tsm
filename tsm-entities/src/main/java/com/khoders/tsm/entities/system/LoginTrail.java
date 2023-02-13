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
import javax.persistence.Table;

/**
 *
 * @author Pascal
 */
@Entity
@Table(name = "login_trail")
public class LoginTrail extends RefNo implements Serializable{
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
    
    @Column(name = "last_logout_time")
    private LocalDateTime lastLogoutTime;
    
    @Column(name = "email_address")
    private String emailAddress;

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public LocalDateTime getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(LocalDateTime lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
}
