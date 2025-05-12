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
public enum Roles implements MsgResolver
{
    SALES_MANAGER("SALES_MANAGER", "Sales Manager"),
    CASHIER("CASHIER", "Cashier"),
    SUPER_ADMINISTRATOR("SUPER_ADMINISTRATOR", "Super Administrator");
    
    private final String label;
    private final String code;
    
    private Roles(String code, String label)
    {
        this.label = label;
        this.code = code;
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
