/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.enums;

import com.khoders.resource.utilities.MsgResolver;

/**
 *
 * @author richard
 */
public enum CustomerType implements MsgResolver
{
   WALK_IN_CUSTOMER("WALK_IN_CUSTOMER", "Walk-In-Customer"),
   BACK_LOG_SUPPLIER("BACK_LOG_SUPPLIER", "Back-Log-Supplier");
   
   private final String code;
   private final String label;
   
   private CustomerType(String code, String label){
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
