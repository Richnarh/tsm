package com.tsm.entities;

import com.dolphindoors.resource.enums.Status;
import com.dolphindoors.resource.enums.Title;
import com.dolphindoors.resource.jpa.BaseModel;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author 
 */
@Entity
@Table(name = "employee")
public class Employee extends BaseModel implements Serializable{
    
    @Column(name = "title")
    @Enumerated(EnumType.STRING)
    private Title title;
    
    @JoinColumn(name = "job_title", referencedColumnName = "id")
    @ManyToOne
    private JobTitle jobTitle;
            
    @Column(name = "first_name")
    private String firstName;
            
    @Column(name = "surname")
    private String surname;
    
    @Column(name = "other_name")
    private String otherName;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    public static final String _email="email";
    @Column(name = "email")
    private String email;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    
}
