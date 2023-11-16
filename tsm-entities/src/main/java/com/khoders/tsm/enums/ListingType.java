/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.enums;

import com.khoders.resource.utilities.MsgResolver;

/**
 *
 * @author Richard Narh
 */
public enum ListingType implements MsgResolver{
    WAREHOUSE_LISTING("WAREHOUSE_LISTING", "Warehouse listing"),
    WAREHOUSE_STOCK_SUMMARY_LISTING("WAREHOUSE_STOCK_SUMMARY_LISTING", "Warehouse Stock Summary Listing"),
    SHOP_INVENTORY_LISTING("SHOP_INVENTORY_LISTING", "Shop Inventory Listing"),
    SHOP_INVENTORY_SUMMARY_LISTING("SHOP_INVENTORY_SUMMARY_LISTING", "Shop Inventory Summary Listing");
    
    private final String code;
    private final String label;
    
    private ListingType(String code,String label){
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
