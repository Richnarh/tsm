/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tsm.dto;

import com.dolphindoors.resource.enums.Status;
import com.dolphindoors.resource.enums.Title;

/**
 *
 * @author richardnarh
 */
public class EmployeeDto extends Base{
    private Title title;
    private String jobTitle;
    private String jobTitleId;
    private String companyBranch;
    private String companyBranchId;
    private String firstName;
    private String fullName;
    private String surname;
    private String otherName;
    private String phoneNumber;
    private String email;
    private byte[] salt;
    private Status status;
    private boolean isAccountCreated;
    private boolean selected;

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(String jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public boolean isIsAccountCreated() {
        return isAccountCreated;
    }

    public void setIsAccountCreated(boolean isAccountCreated) {
        this.isAccountCreated = isAccountCreated;
    }
    
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(String companyBranch) {
        this.companyBranch = companyBranch;
    }

    public String getCompanyBranchId() {
        return companyBranchId;
    }

    public void setCompanyBranchId(String companyBranchId) {
        this.companyBranchId = companyBranchId;
    }
    
}
