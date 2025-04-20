package com.tsm.dto;

import com.tsm.enums.ClientSource;

/**
 *
 * @author richardnarh
 */
public class CustomerDto extends Base{
    private String customerCode;
    private String customerName;
    private ClientSource clientSource;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    private String description;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public ClientSource getClientSource() {
        return clientSource;
    }

    public void setClientSource(ClientSource clientSource) {
        this.clientSource = clientSource;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
