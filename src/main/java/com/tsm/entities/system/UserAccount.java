package com.tsm.entities.system;

import com.dolphindoors.resource.enums.Status;
import com.dolphindoors.resource.jpa.BaseModel;
import com.tsm.entities.Employee;
import com.tsm.enums.Roles;
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
public class UserAccount extends RefNo{
    @JoinColumn(name = "company_branch", referencedColumnName = "id")
    @ManyToOne
    private CompanyBranch companyBranch;
    
    public static final String _employee = "employee";
    @JoinColumn(name = "employee", referencedColumnName = "id")
    @ManyToOne
    private Employee employee;
        
    public static final String _password = "password";
    @Column(name = "password")
    private String password;
    
    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private Roles roles;
    
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }
    
    public CompanyBranch getCompanyBranch()
    {
        return companyBranch;
    }

    public void setCompanyBranch(CompanyBranch companyBranch)
    {
        this.companyBranch = companyBranch;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
}
