package com.khoders.tsm.jbeans.commons;

import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.Location;
import com.khoders.tsm.entities.Product;
import com.khoders.tsm.entities.ProductType;
import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.TransferItem;
import com.khoders.tsm.entities.system.UserAccount;
import com.khoders.tsm.services.InventoryService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.tsm.entities.Packaging;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.UnitMeasurement;
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
@Named(value = "usercommonBeans")
@SessionScoped
public class UsercommonBeans implements Serializable
{
    @Inject private CrudApi crudApi;
    @Inject private InventoryService inventoryService;
    
    private List<Product> productList = new LinkedList<>();
    private List<StockReceiptItem> stockReceiptItemList = new LinkedList<>();
    private List<Customer> customerList = new LinkedList<>();
    private List<UserAccount> userAccountList = new LinkedList<>();
    
    private List<ProductType> productTypeList = new LinkedList<>();
    private List<Location> locationList = new LinkedList<>();
    private List<PurchaseOrder> purchaseOrderList = new LinkedList<>();
    private List<TransferItem> transferItemList = new LinkedList<>();
    private List<Inventory> inventoryList = new LinkedList<>();
    private List<Packaging> packagingList = new LinkedList<>();
    private List<UnitMeasurement> unitMeasurementList = new LinkedList<>();
    private List<Sales> salesList = new LinkedList<>();
    
    @PostConstruct
    public void init(){
        productList = inventoryService.getProductList();
        purchaseOrderList = inventoryService.getPurchaseOrderList();
        stockReceiptItemList = inventoryService.getStockReceiptItemList();
        customerList = inventoryService.getCustomerList();
        userAccountList = inventoryService.getUserAccountList();
        
        productTypeList = inventoryService.getProductTypeList();
        locationList = inventoryService.getLocationList();
        transferItemList = inventoryService.getTransferItemList();
        inventoryList = inventoryService.getInventoryList();
        packagingList = inventoryService.getPackagingList();
        unitMeasurementList = inventoryService.getUnitMeasurementList();
        salesList = inventoryService.getSales();
    }

    public List<Product> getProductList()
    {
        return productList;
    }

    public List<Customer> getCustomerList()
    {
        return customerList;
    }

    public List<StockReceiptItem> getStockReceiptItemList()
    {
        return stockReceiptItemList;
    }

    public void setStockReceiptItemList(List<StockReceiptItem> stockReceiptItemList)
    {
        this.stockReceiptItemList = stockReceiptItemList;
    }
    
    public List<Location> getLocationList() {
        return locationList;
    }

    public List<UserAccount> getUserAccountList() {
        return userAccountList;
    }
    
    public List<ProductType> getProductTypeList()
    {
        return productTypeList;
    }

    public List<PurchaseOrder> getPurchaseOrderList()
    {
        return purchaseOrderList;
    }

    public List<TransferItem> getTransferItemList() {
        return transferItemList;
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public List<Packaging> getPackagingList() {
        return packagingList;
    }

    public List<UnitMeasurement> getUnitMeasurementList() {
        return unitMeasurementList;
    }

    public List<Sales> getSalesList() {
        return salesList;
    }
    
}
