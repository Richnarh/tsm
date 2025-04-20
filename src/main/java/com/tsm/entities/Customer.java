package com.tsm.entities;

import com.dolphindoors.resource.enums.ClientType;
import com.dolphindoors.resource.utilities.JUtils;
import com.tsm.entities.system.UserAccountRecord;
import com.tsm.enums.ClientSource;
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
    private ClientType clientType = ClientType.CUSTOMER;
    
    public static final String _customerName = "customerName";
    @Column(name = "customer_name")
    private String customerName;
    
    public static final String _customerCode = "customerCode";
    @Column(name = "customer_code")
    private String customerCode = JUtils.generate(5);
    
    public static final String _clientSource = "clientSource";
    @Column(name = "client_source")
    @Enumerated(EnumType.STRING)
    private ClientSource clientSource;
    
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

    public ClientSource getClientSource() {
        return clientSource;
    }

    public void setClientSource(ClientSource clientSource) {
        this.clientSource = clientSource;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
    
    @Override
    public String toString()
    {
        return customerName+" "+phone;
    }
}
