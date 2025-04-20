/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.services;

import com.dolphindoors.resource.enums.PaymentStatus;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.DateUtil;
import com.tsm.dto.SalesInventory;
import com.tsm.entities.BatchTransfer;
import com.tsm.entities.Customer;
import com.tsm.entities.Inventory;
import com.tsm.entities.TransferItem;
import com.tsm.entities.Location;
import com.tsm.entities.Product;
import com.tsm.entities.ProductType;
import com.tsm.entities.PurchaseOrder;
import com.tsm.entities.PurchaseOrderItem;
import com.tsm.entities.Sales;
import com.tsm.entities.StockReceipt;
import com.tsm.entities.StockReceiptItem;
import com.tsm.entities.system.UserAccount;
import com.tsm.listener.AppSession;
import com.tsm.entities.PricePackage;
import com.tsm.entities.ReturnItem;
import com.tsm.entities.StockReturn;
import com.tsm.entities.UnitMeasurement;
import com.tsm.enums.LocType;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

/**
 *
 * @author Pascal
 */
@Stateless
public class InventoryService {

    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;
    @Inject private DefaultService ds;

    public List<Product> getProducts() {
        return crudApi.getEm().createQuery("SELECT e FROM Product e ORDER BY e.productName ASC", Product.class).getResultList();
    }

    public List<UnitMeasurement> getUnitMeasurementList(){
       return crudApi.getEm().createQuery("SELECT e FROM UnitMeasurement e ORDER BY e.createdDate DESC", UnitMeasurement.class).getResultList();
    }
    
    public List<Product> getProductList() {
        return crudApi.getEm().createQuery("SELECT e FROM Product e ORDER BY e.productType.productTypeName ASC", Product.class).getResultList();
    }

    public List<Location> getLocationList() {
        return crudApi.getEm().createQuery("SELECT e FROM Location e WHERE e.companyBranch = :companyBranch ORDER BY e.createdDate DESC", Location.class)
                .setParameter(Location._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    
    public List<PricePackage> getPackagingList() {
        return crudApi.getEm().createQuery("SELECT e FROM Packaging e ORDER BY e.createdDate DESC", PricePackage.class).getResultList();
    }

    public List<ProductType> getProductTypeList() {
        return crudApi.getEm().createQuery("SELECT e FROM ProductType e ORDER BY e.createdDate DESC", ProductType.class).getResultList();
    }
    
    public List<Customer> getCustomerList() {
        return crudApi.getEm().createQuery("SELECT e FROM Customer e WHERE e.companyBranch = :companyBranch OR e.companyBranch = NULL ORDER BY e.customerName ASC", Customer.class)
                .setParameter(Customer._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }

    public List<UserAccount> getUserAccountList() {
        return crudApi.getEm().createQuery("SELECT e FROM UserAccount e", UserAccount.class).getResultList();
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        return crudApi.getEm().createQuery("SELECT e FROM PurchaseOrder e WHERE e.companyBranch = :companyBranch", PurchaseOrder.class)
                    .setParameter(PurchaseOrder._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
    }

    public List<StockReceipt> getStockReceiptList() {
            return crudApi.getEm().createQuery("SELECT e FROM StockReceipt e WHERE e.companyBranch = :companyBranch", StockReceipt.class)
                   .setParameter(StockReceipt._companyBranch, appSession.getCompanyBranch())
                   .getResultList();
    }

    public List<StockReceiptItem> getStockReceiptItemList() {
       return crudApi.getEm().createQuery("SELECT e FROM StockReceiptItem e JOIN Product p ON e.product=p WHERE e.companyBranch = :companyBranch GROUP BY p.productName", StockReceiptItem.class)
               .setParameter(StockReceipt._companyBranch, appSession.getCompanyBranch())
               .getResultList();
    }

    public List<PurchaseOrderItem> getPurchaseOrderItem(PurchaseOrder purchaseOrder) {
        return crudApi.getEm().createQuery("SELECT e FROM PurchaseOrderItem e WHERE e.purchaseOrder= :purchaseOrder", PurchaseOrderItem.class)
                .setParameter(PurchaseOrderItem._purchaseOrder, purchaseOrder)
                .setParameter(PurchaseOrderItem._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }

    public List<Sales> getSales() {
       return crudApi.getEm().createQuery("SELECT e FROM Sales e", Sales.class)
               .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
               .getResultList();
    }
    
    public List<Sales> getCreditSales() {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.salesType = :salesType AND e.paymentStatus <> :paymentStatus  AND e.compound = :compound AND e.companyBranch = :companyBranch", Sales.class)
//                    .setParameter(Sales._salesType, SalesType.CREDIT_SALES)
                    .setParameter(Sales._paymentStatus, PaymentStatus.FULLY_PAID)
//                    .setParameter(Sales._compound, false)
                    .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
    }

    public List<Sales> getTotalSumPerDateRange(DateUtil dateRange) {
        return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.valueDate BETWEEN :valueDate AND :valueDate AND e.companyBranch = :companyBranch", Sales.class)
                    .setParameter(Sales._valueDate, dateRange.getFromDate())
                    .setParameter(Sales._valueDate, dateRange.getToDate())
                    .setParameter(Sales._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
    }
    public Customer customertExist(String phone){
       return crudApi.getEm().createQuery("SELECT e FROM Customer e WHERE e.phone= :phone", Customer.class)
                    .setParameter(Customer._phone, phone)
                    .getResultStream().findFirst().orElse(null);
    }

    public List<TransferItem> getTransferItemList(BatchTransfer batchTransfer) {
        return crudApi.getEm().createQuery("SELECT e FROM TransferItem e WHERE e.batchTransfer=:batchTransfer AND e.companyBranch = :companyBranch", TransferItem.class)
                    .setParameter(TransferItem._batchTransfer, batchTransfer)
                    .setParameter(TransferItem._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
    }

    public List<BatchTransfer> getBatchTransferList() {
        return crudApi.getEm().createQuery("SELECT e FROM BatchTransfer e WHERE e.companyBranch = :companyBranch", BatchTransfer.class)
               .setParameter(BatchTransfer._companyBranch, appSession.getCompanyBranch())
               .getResultList();
    }
    
    public List<TransferItem> getTransferItemList() {
        return crudApi.getEm().createQuery("SELECT e FROM TransferItem e WHERE e.companyBranch = :companyBranch", TransferItem.class)
                .setParameter(TransferItem._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    
    public List<Inventory> getInventoryList() {
        return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.companyBranch = :companyBranch ORDER BY e.product.productName ASC", Inventory.class)
               .setParameter(Inventory._companyBranch, appSession.getCompanyBranch())
               .getResultList();
    }
    public List<Inventory> getShopList() {
        Location shop = getShop();
        return crudApi.getEm().createQuery("SELECT e FROM Inventory e WHERE e.location = :location AND e.companyBranch = :companyBranch ORDER BY e.product.productName ASC", Inventory.class)
//               .setParameter(Inventory._location, shop)
               .setParameter(Inventory._companyBranch, appSession.getCompanyBranch())
               .getResultList();
    }
    
    public Location getShop(){
        Location location = ds.getObj(Location.class, Location._locType,  Location._companyBranch, LocType.SHOP, appSession.getCompanyBranch());
        System.out.println("location: "+location);
        return location;
    }

    public Inventory postToInventory(BatchTransfer batchTransfer) {
        List<TransferItem> transferItemList = getTransferItemList(batchTransfer);
        Inventory inventory = null;
        
        for (TransferItem transferItem : transferItemList) {
            inventory = new Inventory();
            inventory.setCompanyBranch(transferItem.getCompanyBranch());
//            inventory.setLocation(transferItem.getBatchTransfer().getFromLocation());
//            inventory.setSellingPrice(transferItem.getStockReceiptItem().getSellingPrice());
//            inventory.setStockReceiptItem(transferItem.getStockReceiptItem());
            inventory.setUserAccount(appSession.getCurrentUser());
            inventory.setLastModifiedBy(appSession.getCurrentUser().getEmailAddress());
//            inventory.setQuantity(transferItem.getQtyTransferred());
            crudApi.save(inventory);
        }
        
        return inventory;
    }

    public List<StockReceiptItem> getStockReceiptItemList(StockReceipt stockReceipt) {
        return crudApi.getEm().createQuery("SELECT e FROM StockReceiptItem e WHERE e.stockReceipt=?1", StockReceiptItem.class)
                .setParameter(StockReceiptItem._stockReceipt, stockReceipt)
                .setParameter(StockReceiptItem._companyBranch, appSession.getCompanyBranch())
                .getResultList();
    }
    public List<StockReturn> getStockReturnList(){
        return crudApi.getEm().createQuery("SELECT e FROM StockReturn e WHERE e.companyBranch= :companyBranch", StockReturn.class)
            .setParameter(StockReturn._companyBranch, appSession.getCompanyBranch())
            .getResultList();
    }

    public List<ReturnItem> getReturnItems(StockReturn stockReturn) {
        return crudApi.getEm().createQuery("SELECT e FROM ReturnItem e WHERE e.stockReturn=:stockReturn AND e.companyBranch= :companyBranch", ReturnItem.class)
                .setParameter(ReturnItem._stockReturn, stockReturn)
                .setParameter(ReturnItem._companyBranch,  appSession.getCompanyBranch())
                .getResultList();
    }
    
}
