/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.admin.commons;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.tsm.admin.services.CompanyService;
import com.khoders.tsm.admin.services.PermissionService;
import com.khoders.tsm.entities.Location;
import com.khoders.tsm.entities.Product;
import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.UnitMeasurement;
import com.khoders.tsm.entities.system.CompanyBranch;
import com.khoders.tsm.entities.system.CompanyProfile;
import com.khoders.tsm.entities.system.AppPage;
import com.khoders.tsm.entities.system.UserAccount;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "userCommons")
@SessionScoped
public class UserCommons implements Serializable
{
   @Inject private CrudApi crudApi;
   @Inject private CompanyService companyService;
   @Inject private PermissionService permissionService;
   
   private List<CompanyBranch> companyBranchList = new LinkedList<>();
   private List<CompanyProfile> companyProfileList = new LinkedList<>();
   private List<UserAccount> userAccountList = new LinkedList<>();
   private List<AppPage> appPageList = new LinkedList<>();
   private List<String> tableList = new LinkedList<>();
   private List<Location> locationList = new LinkedList<>();
   private List<PurchaseOrder> purchaseOrderList = new LinkedList<>();
   private List<Product> productList = new LinkedList<>();
   private List<UnitMeasurement> unitMeasurementList = new LinkedList<>();
   
   @PostConstruct
   public void init(){
       companyBranchList = companyService.getCompanyBranchList();
       companyProfileList = companyService.getCompanyProfileList();
       userAccountList = companyService.getUserAccountList();
       appPageList = permissionService.getAppPageList();
       tableList = companyService.getTables();
       locationList = crudApi.findAll(Location.class);
       purchaseOrderList = crudApi.findAll(PurchaseOrder.class);
       productList = crudApi.findAll(Product.class);
       unitMeasurementList = crudApi.findAll(UnitMeasurement.class);
   }
   
    public List<CompanyBranch> getCompanyBranchList()
    {
        return companyBranchList;
    }

    public List<CompanyProfile> getCompanyProfileList()
    {
        return companyProfileList;
    }

    public List<UserAccount> getUserAccountList()
    {
        return userAccountList;
    }

    public List<AppPage> getAppPageList()
    {
        return appPageList;
    }

    public List<String> getTableList() {
        return tableList;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public List<UnitMeasurement> getUnitMeasurementList() {
        return unitMeasurementList;
    }
    
}
