package com.tsm.entities.system;

import com.dolphindoors.resource.enums.AccessLevel;
import com.dolphindoors.resource.enums.Status;
import com.dolphindoors.resource.jpa.BaseModel;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author richa
 */
@Entity
@Table(name = "user_account")
public class UserAccount extends BaseModel
{
    @JoinColumn(name = "company_branch", referencedColumnName = "id")
    @ManyToOne
    private CompanyBranch companyBranch;
        
    @Column(name = "fullname")
    private String fullname;
    
    public static final String _emailAddress = "emailAddress";
    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "phone_number")
    private String phoneNumber;

    public static final String _password = "password";
    @Column(name = "password")
    private String password;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    
    @Column(name = "access_level")
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;
    
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public CompanyBranch getCompanyBranch()
    {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch)
    {
        this.companyBranch = companyBranch;
    }

    public String getFullname()
    {
        return fullname;
    }

    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    @Override
    public String toString() {
        return emailAddress;
    }
    
}
