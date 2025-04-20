/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.enums;

import com.dolphindoors.resource.utilities.MsgResolver;

/**
 *
 * @author Pascal
 */
public enum TransferStatus implements MsgResolver{
    PENDING("PENDING", "Pending"),
    APPROVED("APPROVED", "Approved"),
    REJECTED("REJECTED", "Rejected"),
    ACCEPTED("ACCEPTED", "Accepted");
    
    private final String code;
    private final String label;
    
    private TransferStatus(String code, String label){
        this.code = code;
        this.label = label;
    }

    @Override
    public String getCode() {
       return code;
    }

    @Override
    public String getLabel() {
       return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
