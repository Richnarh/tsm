
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
import com.khoders.tsm.enums.ReceiptStatus;
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
   private Location selectedLocation = null;
   private PurchaseOrder selectedPurchaseOrder = null;
   private List<StockReceipt> stockReceiptList = new LinkedList<>();
   private List<PurchaseOrderItem> purchaseOrderItemList = new LinkedList<>();
   private List<StockReceiptItem> stockReceiptItemList = new LinkedList<>();
   private List<PurchaseOrder> purchaseOrderList = new LinkedList<>();
   
   private ReceiptStatus receiptStatus = ReceiptStatus.PENDING;
   
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
   
   public void initOrder(){
     purchaseOrderList = inventoryService.getPurchaseOrderList();
   }
   
   public void applyReceipt(StockReceiptItem stockReceiptItem){
       System.out.println("Qty: "+stockReceiptItem.getPkgQuantity());
       System.out.println("Cost price: "+stockReceiptItem.getCostPrice());
       
        if (crudApi.save(stockReceipt) != null) {
            stockReceiptItem.setStockReceipt(stockReceipt);
            stockReceiptItem.genCode();
            stockReceiptItem.setReceiptStatus(ReceiptStatus.RECEIVED);
            stockReceiptItem.setCompanyBranch(appSession.getCompanyBranch());
            stockReceiptItem.setUserAccount(appSession.getCurrentUser());
            stockReceiptItem.setLastModifiedBy(appSession.getCurrentUser() != null ? appSession.getCurrentUser().getFullname() : null);
            receiptStatus = ReceiptStatus.RECEIVED;
            crudApi.save(stockReceiptItem);
        }
   }
   
   public void finalise(){
       stockReceiptItemList = inventoryService.getStockReceiptItemList(stockReceipt);
       stockReceipt.setTotalAmount(stockReceiptItemList.stream().mapToDouble(StockReceiptItem::getCostPrice).sum());
       
       if(crudApi.save(stockReceipt) != null)
            Msg.info("Receipt finalised with total amount updated!");
       else
           Msg.error("Oops! could not finalise receipt");
   }
    
    public void selectPurchaseOrder(PurchaseOrder purchaseOrder){
      selectedPurchaseOrder = purchaseOrder;
      stockReceiptItemList = new LinkedList<>();
      clearStockReceipt();
      stockReceipt = stockService.getStockReceipt(purchaseOrder);
      if(stockReceipt == null){
        stockReceipt = new StockReceipt();
        stockReceipt.setPurchaseOrder(purchaseOrder);
        stockReceipt.setTotalAmount(purchaseOrder.getTotalAmount());
        stockReceipt.setBatchNo(SystemUtils.generateCode());
        stockReceipt.setLocation(purchaseOrder.getLocation());
        stockReceipt.setUserAccount(appSession.getCurrentUser());
        stockReceipt.setReceivedBy(appSession.getCurrentUser());
        stockReceipt.setCompanyBranch(appSession.getCompanyBranch());
        stockReceipt.setRefNo(purchaseOrder.getOrderCode());
        stockReceipt.setStockSaved(true);
        
        stockReceiptItemList = xtractService.extractStockReceiptItems(purchaseOrder,stockReceipt);
        
      }else{
//          List<StockReceiptItem> receiptItemList = xtractService.extractStockReceiptItems(purchaseOrder,stockReceipt);
          stockReceiptItemList = inventoryService.getStockReceiptItemList(stockReceipt);
//          if(receiptItemList.size() > stockReceiptItemList.size()){
//              Set<StockReceiptItem> copyReceiptList = new LinkedHashSet<>(stockReceiptItemList);
//              System.out.println("copyReceipt Before: "+copyReceiptList.size());
////              copyReceiptList.removeAll(stockReceiptItemList);
//              
//              System.out.println("receiptItemList:::::: "+receiptItemList.size());
//              System.out.println("receiptItemList:::::: "+receiptItemList.toString());
//              
//              receiptItemList.addAll(copyReceiptList);
//              System.out.println("receiptItemList After: "+receiptItemList.size());
//              System.out.println("receiptItemList: "+receiptItemList.toString());
//              
//              stockReceiptItemList = new LinkedList<>();
//              stockReceiptItemList.addAll(receiptItemList);
//          }
      }
    }

   public void saveStockReceipt(){
       try{
           stockReceipt.setBatchNo(stockReceipt.getBatchNo());
           if (crudApi.save(stockReceipt) != null) {
               stockReceiptItemList.forEach(item -> {
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

    public Location getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(Location selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
    
    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public ReceiptStatus getReceiptStatus() {
        return receiptStatus;
    }
    
}
