/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.enums;

import com.dolphindoors.resource.utilities.MsgResolver;

/**
 *
 * @author richa
 */
public enum InvoiceType implements MsgResolver
{
    PROFORMA_INVOICE("PROFORMA_INVOICE", "PROFORMA INVOICE"),
    INVOICE("INVOICE", "INVOICE");
    
    private final String code;
    private final String label;
    
    private InvoiceType(String code,String label){
        this.code=code;
        this.label=label;
    }

    @Override
    public String getCode()
    {
       return code;
    }

    @Override
    public String getLabel()
    {
       return label;
    }

    @Override
    public String toString()
    {
        return label;
    }
    
}
