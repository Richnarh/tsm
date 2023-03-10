/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khoders.tsm.services;

import com.khoders.resource.enums.PaymentStatus;
import com.khoders.tsm.entities.BatchTransfer;
import com.khoders.tsm.entities.Customer;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.TransferItem;
import com.khoders.tsm.entities.Location;
import com.khoders.tsm.entities.Product;
import com.khoders.tsm.entities.ProductType;
import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.PurchaseOrderItem;
import com.khoders.tsm.entities.SaleItem;
import com.khoders.tsm.entities.Sales;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.system.UserAccount;
import com.khoders.tsm.listener.AppSession;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.DateRangeUtil;
import com.khoders.tsm.entities.Packaging;
import com.khoders.tsm.entities.ReturnItem;
import com.khoders.tsm.entities.StockReturn;
import com.khoders.tsm.entities.UnitMeasurement;
import com.khoders.tsm.enums.SalesType;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;

/**
 *
 * @author Pascal
 */
@Stateless
public class InventoryService {

    @Inject private CrudApi crudApi;
    @Inject private AppSession appSession;

    public List<Product> getProducts() {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM Product e ORDER BY e.productName ASC", Product.class).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<UnitMeasurement> getUnitMeasurementList()
    {
        try
        {
            return crudApi.getEm().createQuery("SELECT e FROM UnitMeasurement e ORDER BY e.createdDate DESC", UnitMeasurement.class).getResultList();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    public List<Product> getProductList() {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM Product e ORDER BY e.productType.productTypeName ASC", Product.class).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<Location> getLocationList() {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM Location e ORDER BY e.createdDate DESC", Location.class).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    public List<Packaging> getPackagingList() {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM Packaging e ORDER BY e.createdDate DESC", Packaging.class).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<ProductType> getProductTypeList() {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM ProductType e ORDER BY e.createdDate DESC", ProductType.class).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<Customer> getCustomerList() {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM Customer e WHERE e.companyBranch = :companyBranch ORDER BY e.customerName ASC", Customer.class)
                    .setParameter(Customer._companyBranch, appSession.getCompanyBranch())
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<UserAccount> getUserAccountList() {
        try {
            String qryString = "SELECT e FROM UserAccount e";
            TypedQuery<UserAccount> typedQuery = crudApi.getEm().createQuery(qryString, UserAccount.class);
            return typedQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM PurchaseOrder e WHERE e.companyBranch = :companyBranch", PurchaseOrder.class)
                    .setParameter(PurchaseOrder._companyBranch, appSession.getCompanyBranch())
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<StockReceipt> getStockReceiptList() {
            return crudApi.getEm().createQuery("SELECT e FROM StockReceipt e WHERE e.companyBranch = :companyBranch", StockReceipt.class)
                   .setParameter(StockReceipt._companyBranch, appSession.getCompanyBranch())
                   .getResultList();
    }

    public List<StockReceiptItem> getStockReceiptItemList() {
        try {
            String qryString = "SELECT e FROM StockReceiptItem e JOIN Product p ON e.product=p GROUP BY p.productName";
            TypedQuery<StockReceiptItem> typedQuery = crudApi.getEm().createQuery(qryString, StockReceiptItem.class);
            return typedQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<PurchaseOrderItem> getPurchaseOrderItem(PurchaseOrder purchaseOrder) {
        try {
            TypedQuery<PurchaseOrderItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM PurchaseOrderItem e WHERE e.purchaseOrder=?1", PurchaseOrderItem.class);
            typedQuery.setParameter(1, purchaseOrder);

            return typedQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Sales> getSales() {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM Sales e", Sales.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<Sales> getCreditSales() {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.salesType = :salesType AND e.paymentStatus <> :paymentStatus  AND e.compound = :compound", Sales.class)
                    .setParameter(Sales._salesType, SalesType.CREDIT_SALES)
                    .setParameter(Sales._paymentStatus, PaymentStatus.FULLY_PAID)
                    .setParameter(Sales._compound, false)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<SaleItem> getSales(Sales sales) {
        try {
            TypedQuery<SaleItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM SaleItem e WHERE e.sales=:sales", SaleItem.class);
            typedQuery.setParameter(SaleItem._sales, sales);
            return typedQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Sales> getTotalSumPerDateRange(DateRangeUtil dateRange) {
        try {
            return crudApi.getEm().createQuery("SELECT e FROM Sales e WHERE e.valueDate BETWEEN ?1 AND ?2 ", Sales.class)
                    .setParameter(1, dateRange.getFromDate())
                    .setParameter(2, dateRange.getToDate())
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
    public Customer customertExist(String phone)
    {
        try
        {
            String qryString = "SELECT e FROM Customer e WHERE e.phone=?1";
            TypedQuery<Customer> typedQuery = crudApi.getEm().createQuery(qryString, Customer.class)
                    .setParameter(1, phone);
            
                    return typedQuery.getResultStream().findFirst().orElse(null);
                    
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public List<TransferItem> getTransferItemList(BatchTransfer batchTransfer) {
        try {
            TypedQuery<TransferItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM TransferItem e WHERE e.batchTransfer=:batchTransfer", TransferItem.class);
            typedQuery.setParameter("batchTransfer", batchTransfer);
            return typedQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<BatchTransfer> getBatchTransferList() {
        try {
            TypedQuery<BatchTransfer> typedQuery = crudApi.getEm().createQuery("SELECT e FROM BatchTransfer e", BatchTransfer.class);
            return typedQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<TransferItem> getTransferItemList() {
        try {
            TypedQuery<TransferItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM TransferItem e", TransferItem.class);
            return typedQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    
    public List<Inventory> getInventoryList() {
        try {
//            TypedQuery<Inventory> typedQuery = crudApi.getEm().createQuery("SELECT e FROM Inventory e GROUP BY e.stockReceiptItem ORDER BY e.stockReceiptItem ASC", Inventory.class);
            TypedQuery<Inventory> typedQuery = crudApi.getEm().createQuery("SELECT e FROM Inventory e ORDER BY e.stockReceiptItem ASC", Inventory.class);
            return typedQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public Inventory postToInventory(BatchTransfer batchTransfer) {
        List<TransferItem> transferItemList = getTransferItemList(batchTransfer);
        Inventory inventory = null;
        
        for (TransferItem transferItem : transferItemList) {
            inventory = new Inventory();
            inventory.setCompanyBranch(transferItem.getCompanyBranch());
            inventory.setLocation(transferItem.getBatchTransfer().getFromLocation());
//            inventory.setSellingPrice(transferItem.getStockReceiptItem().getSellingPrice());
            inventory.setStockReceiptItem(transferItem.getStockReceiptItem());
            inventory.setUserAccount(appSession.getCurrentUser());
            inventory.setLastModifiedBy(appSession.getCurrentUser().getEmailAddress());
//            inventory.setQuantity(transferItem.getQtyTransferred());
            crudApi.save(inventory);
        }
        
        return inventory;
    }

    public List<StockReceiptItem> getStockReceiptItemList(StockReceipt stockReceipt) {
        try {
            TypedQuery<StockReceiptItem> typedQuery = crudApi.getEm().createQuery("SELECT e FROM StockReceiptItem e WHERE e.stockReceipt=?1", StockReceiptItem.class);
            typedQuery.setParameter(1, stockReceipt);

            return typedQuery.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
    public List<StockReturn> getStockReturnList()
    {
        try
        {
            String qryString = "SELECT e FROM StockReturn e WHERE  e.companyBranch=?1";
            TypedQuery<StockReturn> typedQuery = crudApi.getEm().createQuery(qryString, StockReturn.class);
                                typedQuery.setParameter(1, appSession.getCompanyBranch());
                            return typedQuery.getResultList();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public List<ReturnItem> getReturnItems(StockReturn stockReturn) {
        String qryString = "SELECT e FROM ReturnItem e WHERE e.stockReturn=:stockReturn";
        TypedQuery<ReturnItem> typedQuery = crudApi.getEm().createQuery(qryString, ReturnItem.class);
                            typedQuery.setParameter("stockReturn", stockReturn);
                        return typedQuery.getResultList();
    }
}
