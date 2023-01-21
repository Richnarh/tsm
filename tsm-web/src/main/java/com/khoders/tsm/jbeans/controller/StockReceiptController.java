
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.entities.Location;
import com.khoders.tsm.entities.PurchaseOrder;
import com.khoders.tsm.entities.PurchaseOrderItem;
import com.khoders.tsm.entities.StockReceipt;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.InventoryService;
import com.khoders.tsm.services.StockService;
import com.khoders.tsm.services.XtractService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import java.io.Serializable;
import java.time.LocalDate;
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
@Named(value = "stockReceiptController")
@SessionScoped
public class StockReceiptController implements Serializable
{
   @Inject private CrudApi crudApi;
   @Inject private InventoryService inventoryService;
   @Inject private AppSession appSession;
   @Inject private StockService stockService;
   @Inject private XtractService xtractService;
   
   private StockReceipt stockReceipt = new StockReceipt();
   private Location location = new Location();
   private PurchaseOrder selectedPurchaseOrder = null;
   private List<StockReceipt> stockReceiptList = new LinkedList<>();
   private List<PurchaseOrderItem> purchaseOrderItemList = new LinkedList<>();
   private List<StockReceiptItem> stockReceiptItemList = new LinkedList<>();
   
   private String optionText;
   private LocalDate expiryDate;
   private boolean savedStock = false;
   private double sellingPrice;
   
   @PostConstruct
   private void init()
   {
     clearStockReceipt();
     stockReceiptList = inventoryService.getStockReceiptList();
   }
    
    public void receivePurchaseOrder(PurchaseOrder purchaseOrder){
      selectedPurchaseOrder = purchaseOrder;
      stockReceiptItemList = new LinkedList<>();
      clearStockReceipt();
      stockReceipt = stockService.getStockReceipt(purchaseOrder);
      if(stockReceipt == null){
        stockReceipt = new StockReceipt();
        stockReceipt.setPurchaseOrder(purchaseOrder);
        stockReceipt.setTotalAmount(purchaseOrder.getTotalAmount());
        stockReceipt.setBatchNo(SystemUtils.generateCode());
        stockReceipt.setUserAccount(appSession.getCurrentUser());
        stockReceipt.setReceivedBy(appSession.getCurrentUser());
        stockReceipt.setCompanyBranch(appSession.getCompanyBranch());
        stockReceipt.setStockSaved(true);
        savedStock = true;
      }else{
        savedStock = false;
      }
      
      purchaseOrderItemList = inventoryService.getPurchaseOrderItem(purchaseOrder);
        for (PurchaseOrderItem item : purchaseOrderItemList) {
            StockReceiptItem receiptItem = new StockReceiptItem();
            receiptItem.setCostPrice(item.getCostPrice());
            receiptItem.setSellingPrice(0.0);
            receiptItem.setProduct(item.getProduct());
            receiptItem.setPurchaseOrderItem(item);
            receiptItem.setStockReceipt(stockReceipt);
            receiptItem.setPkgQuantity(item.getQtyPurchased());
            receiptItem.setDescription(item.getDescription());
            receiptItem.setUserAccount(appSession.getCurrentUser());
            receiptItem.setCompanyBranch(appSession.getCompanyBranch());
            receiptItem.genCode();
            stockReceiptItemList.add(receiptItem);
        }
      
    }

   public void saveStockReceipt(){
       try{
           stockReceipt.setBatchNo(stockReceipt.getBatchNo());
           if (crudApi.save(stockReceipt) != null) {
               stockReceiptItemList.forEach(item -> {
                   item.setSellingPrice(sellingPrice);
                   crudApi.save(item);
               });
               PurchaseOrder purchaseOrder = crudApi.find(PurchaseOrder.class, stockReceipt.getPurchaseOrder().getId());
               if (purchaseOrder != null) {
                   purchaseOrder.setStockFullyReceived(true);
                   crudApi.save(purchaseOrder);
               }
               Msg.info(Msg.SUCCESS_MESSAGE);
           }
       } catch (Exception e){
           e.printStackTrace();
       }
   }
   
   public void deleteStockReceipt(StockReceipt stockReceipt){
       try{
         if(crudApi.delete(stockReceipt)){
          stockReceiptList.remove(stockReceipt);
          Msg.info(Msg.SUCCESS_MESSAGE);
         }  
       } catch (Exception e)
       {
         e.printStackTrace();
       }
   }
   
   public void editStockReceipt(StockReceipt stockReceipt){
       this.stockReceipt = stockReceipt;
   }

    public void clearStockReceipt()
    {
        stockReceipt = new StockReceipt();
        stockReceipt.setUserAccount(appSession.getCurrentUser());
        stockReceipt.setCompanyBranch(appSession.getCompanyBranch());
        purchaseOrderItemList = new LinkedList<>();
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public StockReceipt getStockReceipt()
    {
      return stockReceipt;
    }

    public void setStockReceipt(StockReceipt stockReceipt)
    {
        this.stockReceipt = stockReceipt;
    }

    public List<StockReceipt> getStockReceiptList()
    {
        return stockReceiptList;
    }

    public String getOptionText()
    {
        return optionText;
    }

    public List<PurchaseOrderItem> getPurchaseOrderItemList() {
        return purchaseOrderItemList;
    }
    
    public LocalDate getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public PurchaseOrder getSelectedPurchaseOrder()
    {
        return selectedPurchaseOrder;
    }

    public void setSelectedPurchaseOrder(PurchaseOrder selectedPurchaseOrder)
    {
        this.selectedPurchaseOrder = selectedPurchaseOrder;
    }

    public boolean isSavedStock()
    {
        return savedStock;
    }

    public void setSavedStock(boolean savedStock)
    {
        this.savedStock = savedStock;
    }

    public List<StockReceiptItem> getStockReceiptItemList() {
        return stockReceiptItemList;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
}
