package com.tsm.jbeans.commons;

import com.dolphindoors.resource.jpa.CrudApi;
import com.tsm.entities.Customer;
import com.tsm.entities.Inventory;
import com.tsm.entities.Location;
import com.tsm.entities.Product;
import com.tsm.entities.ProductType;
import com.tsm.entities.PurchaseOrder;
import com.tsm.entities.StockReceiptItem;
import com.tsm.entities.TransferItem;
import com.tsm.entities.system.UserAccount;
import com.tsm.services.InventoryService;
import com.tsm.entities.PricePackage;
import com.tsm.entities.Sales;
import com.tsm.entities.UnitMeasurement;
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
    private List<Inventory> shopList = new LinkedList<>();
    private List<PricePackage> packagingList = new LinkedList<>();
    private List<UnitMeasurement> unitMeasurementList = new LinkedList<>();
    private List<Sales> creditSalesList = new LinkedList<>();
    
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
        shopList = inventoryService.getShopList();
        packagingList = inventoryService.getPackagingList();
        unitMeasurementList = inventoryService.getUnitMeasurementList();
        creditSalesList = inventoryService.getCreditSales();
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

    public List<PricePackage> getPackagingList() {
        return packagingList;
    }

    public List<UnitMeasurement> getUnitMeasurementList() {
        return unitMeasurementList;
    }

    public List<Sales> getCreditSalesList() {
        return creditSalesList;
    }

    public List<Inventory> getShopList() {
        return shopList;
    }
    
}
