/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.lookups;

import com.dolphindoors.resource.jpa.CrudApi;
import com.lestieshop.lookups.LookupItem;
import com.tsm.entities.Customer;
import com.tsm.entities.Employee;
import com.tsm.entities.Inventory;
import com.tsm.entities.JobTitle;
import com.tsm.entities.Packaging;
import com.tsm.entities.PricePackage;
import com.tsm.entities.Product;
import com.tsm.entities.ProductType;
import com.tsm.entities.system.CompanyProfile;
import com.tsm.entities.system.UserAccount;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Pascal
 */
@Stateless
public class LookupService {
    @Inject private CrudApi crudApi;
    
    public List<LookupItem> companyProfile() {
        List<LookupItem> itemList = new LinkedList<>();
        crudApi.findAll(CompanyProfile.class).forEach(data -> {
            LookupItem item = new LookupItem();
            item.setId(data.getId());
            item.setItemName(data.getCompanyEmail()+"-"+data.getTinNo());
            itemList.add(item);
        });
        return itemList;
    } 
   
    public List<LookupItem> inventory() {
        List<LookupItem> itemList = new LinkedList<>();
        crudApi.findAll(Inventory.class).forEach(data -> {
            LookupItem item = new LookupItem();
            item.setId(data.getId());
            item.setItemName(data.getProduct()+"");
            itemList.add(item);
        });
        return itemList;
    } 
    public List<LookupItem> products() {
        List<LookupItem> itemList = new LinkedList<>();
        crudApi.findAll(Product.class).forEach(data -> {
            LookupItem item = new LookupItem();
            item.setId(data.getId());
            if(data.getProductType() != null && data.getProductType().getProductTypeName() != null)
                item.setItemName(data.getProductName() +" - "+data.getProductType().getProductTypeName());
            else
                item.setItemName(data.getProductName());
            itemList.add(item);
        });
        return itemList;
    } 
    public List<LookupItem> productTypes() {
        List<LookupItem> itemList = new LinkedList<>();
        crudApi.findAll(ProductType.class).forEach(data -> {
            LookupItem item = new LookupItem();
            item.setId(data.getId());
            item.setItemName(data.getProductTypeName());
            itemList.add(item);
        });
        return itemList;
    } 
    public List<LookupItem> users() {
        List<LookupItem> itemList = new LinkedList<>();
        crudApi.findAll(UserAccount.class).forEach(data -> {
            LookupItem item = new LookupItem();
            item.setId(data.getId());
            item.setItemName(data.getFullname() +" - "+data.getPhoneNumber());
            itemList.add(item);
        });
        return itemList;
    } 
    public List<LookupItem> employees() {
        List<LookupItem> itemList = new LinkedList<>();
        crudApi.findAll(Employee.class).forEach(data -> {
            LookupItem item = new LookupItem();
            item.setId(data.getId());
            if(data.getSurname() == null && data.getOtherName() != null){
                item.setItemName(data.getFirstName() +" "+data.getOtherName());
            }else if(data.getOtherName() == null && data.getSurname() != null){
                item.setItemName(data.getFirstName() +" "+data.getSurname());
            }else if(data.getSurname() == null && data.getOtherName() == null){
                item.setItemName(data.getFirstName());
            }else{
                item.setItemName(data.getFirstName() +" "+data.getSurname() +" "+data.getOtherName());
            }
            itemList.add(item);
        });
        return itemList;
    } 
    public List<LookupItem> jobTitles() {
        List<LookupItem> itemList = new LinkedList<>();
        crudApi.findAll(JobTitle.class).forEach(data -> {
            LookupItem item = new LookupItem();
            item.setId(data.getId());
            item.setItemName(data.getTitleName());
            itemList.add(item);
        });
        return itemList;
    } 
   
    public List<LookupItem> customers() {
        List<LookupItem> itemList = new LinkedList<>();
        crudApi.findAll(Customer.class).forEach(data -> {
            LookupItem item = new LookupItem();
            item.setId(data.getId());
            item.setItemName(data+"");
            itemList.add(item);
        });
        return itemList;
    } 
    
    public List<LookupItem> packaging() {
        List<LookupItem> itemList = new LinkedList<>();
        crudApi.findAll(Packaging.class).forEach(data -> {
            LookupItem item = new LookupItem();
            item.setId(data.getId());
            item.setItemName(data.getPackagingName());
            itemList.add(item);
        });
        return itemList;
    } 
    
    public List<LookupItem> pricepPackage() {
        List<LookupItem> itemList = new LinkedList<>();
        crudApi.findAll(PricePackage.class).forEach(data -> {
            LookupItem item = new LookupItem();
            item.setId(data.getId());
            item.setItemName(data.getPackaging()+"");
            itemList.add(item);
        });
        return itemList;
    } 
    
}
