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
public enum EventModule implements MsgResolver{
   
    APP_PAGE("APP_PAGE", "app_page"),
    BATCH_TRANSFER("BATCH_TRANSFER", "batch_transfer"),         
    COMPANY_BRANCH("COMPANY_BRANCH", "company_branch"),         
    COMPANY_PROFILE("COMPANY_PROFILE", "company_profile"),        
    COMPOUND_SALE("COMPOUND_SALE", "compound_sale"),          
    CREDIT_PAYMENT("CREDIT_PAYMENT", "credit_payment"),         
    CUSTOMER("CUSTOMER", "customer"),               
    EVENT_LOG("EVENT_LOG", "event_log"),              
    INVENTORY("INVENTORY", "inventory"),              
    LOCATION("LOCATION", "location"),               
    PACKAGING("PACKAGING", "packaging"),              
    PAGE_ACTION("PAGE_ACTION", "page_action"),            
    PERMISSION("PERMISSION", "permission"),             
    PRODUCT("PRODUCT", "product"),                
    PRODUCT_TYPE("PRODUCT_TYPE", "product_type"),           
    PURCHASE_ORDER("PURCHASE_ORDER", "purchase_order"),         
    PURCHASE_ORDER_ITEM("PURCHASE_ORDER_ITEM", "purchase_order_item"),    
    RETURN_ITEM("RETURN_ITEM", "return_item"),            
    SALES("SALES", "sales"),                  
    SALES_ADDITIONAL_INFO("SALES_ADDITIONAL_INFO", "sales_additional_info"),  
    SALES_TAX("SALES_TAX", "sales_tax"),              
    SALE_ITEM("SALE_ITEM", "sale_item"),              
    STOCK_RECEIPT("STOCK_RECEIPT", "stock_receipt"),          
    STOCK_RECEIPT_ITEM("STOCK_RECEIPT_ITEM", "stock_receipt_item"),     
    STOCK_RETURN("STOCK_RETURN", "stock_return"),           
    TAX("TAX", "tax"),                    
    TRANSFER_ITEM("TRANSFER_ITEM", "transfer_item"),          
    UNIT_MEASUREMENT("UNIT_MEASUREMENT", "unit_measurement"),       
    USER_ACCOUNT("USER_ACCOUNT", "user_account"),           
    USER_PAGES("USER_PAGES", "user_pages"),             
    USER_PAGE_ACTION("USER_PAGE_ACTION", "user_page_action");   
        
   private final String code;
   private final String label;
   
   private EventModule(String code, String label){
       this.code=code;
       this.label = label;
   }
   
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getCode() {
       return code;
    }

    @Override
    public String toString() {
        return label;
    }
}
