/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.enums;

import com.khoders.resource.utilities.MsgResolver;

/**
 *
 * @author Pascal
 */
public enum SalesType implements MsgResolver{
    INSTANT_SALES("INSTANT_SALES", "Instant Sales"),
    INVOICE_SALES("INVOICE_SALES", "Invoice Sales"),
    PROFORMA_INVOICE_SALES("PROFORMA_INVOICE_SALES", "Proforma Invoice Sales"),
    CREDIT_SALES("CREDIT_SALES", "Credit Sales");
    
    private final String code;
    private final String label;
    
    private SalesType(String code, String label){
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
