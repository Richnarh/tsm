package com.khoders.tsm.entities;

import com.khoders.resource.enums.ClientType;
import com.khoders.tsm.entities.system.UserAccountRecord;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 *
 * @author richard
 */
@Entity
@Table(name = "customer")
public class Customer extends UserAccountRecord
{
    public static final String _clientType = "clientType";
    @Column(name = "client_type")
    @Enumerated(EnumType.STRING)
    private ClientType clientType;
    
    public static final String _customerName = "customerName";
    @Column(name = "customer_name")
    private String customerName;
    
    public static final String _phone = "phone";
    @Column(name = "phone")
    private String phone;
    
    public static final String _emailAddress = "emailAddress";
    @Column(name = "email_address")
    private String emailAddress;
    
    public static final String _address = "address";
    @Column(name = "address")
    private String address;

    @Column(name = "description")
    @Lob
    private String description;      

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public ClientType getClientType()
    {
        return clientType;
    }

    public void setClientType(ClientType clientType)
    {
        this.clientType = clientType;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }
    @Override
    public String toString()
    {
        return customerName+" "+phone;
    }
}
