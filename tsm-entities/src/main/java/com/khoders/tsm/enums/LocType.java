/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.enums;

import com.khoders.resource.utilities.MsgResolver;

/**
 *
 * @author richa
 */
public enum LocType implements MsgResolver
{
    WAREHOUSE("WAREHOUSE", "Warehouse"),
    SHOP("SHOP", "Shop");
    
    private final String code;
    private final String label;
    
    private LocType(String code,String label){
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
