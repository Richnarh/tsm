
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.services.InventoryService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.Inventory;
import com.khoders.tsm.entities.StockReceiptItem;
import com.khoders.tsm.entities.UnitMeasurement;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.StockService;
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
@Named(value = "inventoryController")
@SessionScoped
public class InventoryController implements Serializable
{
   @Inject private CrudApi crudApi;
   @Inject private AppSession appSession;
   @Inject private InventoryService inventoryService;
   @Inject private StockService stockService;
   
   private Inventory inventory = new Inventory();
   private List<Inventory> inventoryList = new LinkedList<>();
   private List<Inventory> segmentedList = new LinkedList<>();
   private List<StockReceiptItem> stockReceiptItemList = new LinkedList<>();
   private String optionText;
   private StockReceiptItem selectedStockReceiptItem = null;
   private UnitMeasurement selectedUnit = null;
   
   @PostConstruct
   private void init(){
     clearInventory();  
   }
   
   public void initProduct(){     
     stockReceiptItemList = inventoryService.getStockReceiptItemList();
   }
   
   public void initInventory(){
     inventoryList = inventoryService.getInventoryList();
   }
   
   public void selectProduct(StockReceiptItem stockReceiptItem){
       selectedStockReceiptItem=stockReceiptItem;
       segmentedList = stockService.inventoryProduct(selectedStockReceiptItem);
       
       inventory.setWprice(stockReceiptItem.getWprice());
   }
   
   public void updateUnit(){
       selectedUnit = inventory.getUnitMeasurement();
   }
   
   public void saveInventory()
   {
       if(selectedStockReceiptItem == null){
           Msg.error("Please select a product");
           return;
       }
       try
       {
           if(optionText.equals("Save Changes")){
              Inventory newPackage = stockService.existProdctPackage(selectedStockReceiptItem, inventory.getUnitMeasurement().getUnits());
              if (newPackage != null){
                  Msg.error("product and the package already exist");
                  return;
              }
          }
           
           if(inventory != null && inventory.getUnitsInPackage() == 0.0){
               Msg.error("Units in package is required");
               return;
           }
           if(inventory != null && inventory.getPackagePrice() == 0.0){
               Msg.error("Selling price is required");
               return;
           }
          inventory.genCode();
          inventory.setStockReceiptItem(selectedStockReceiptItem);
          if(crudApi.save(inventory) != null){
              inventoryList = CollectionList.washList(inventoryList, inventory);
               Msg.info(Msg.SUCCESS_MESSAGE);
          }
          clearInventory();
       } catch (Exception e)
       {
          e.printStackTrace();
       }
   }
   
   public void deleteInventory(Inventory inventory)
   {
       try
       {
         if(crudApi.delete(inventory))
         {
             inventoryList.remove(inventory);
             Msg.info(Msg.SUCCESS_MESSAGE);
         }  
       } catch (Exception e)
       {
         e.printStackTrace();
       }
   }
   
   public void editInventory(Inventory inventory){
       this.inventory = inventory;
       selectedStockReceiptItem = inventory.getStockReceiptItem();
       this.optionText = "Update";
   }

    public void clearInventory()
    {
        inventory = new Inventory();
        inventory.setUserAccount(appSession.getCurrentUser());
        inventory.setCompanyBranch(appSession.getCompanyBranch());
        selectedStockReceiptItem = null;
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public void updateWp(){
         stockReceiptItemList.forEach(item ->{
            inventoryList = inventoryService.getInventoryList();
            inventoryList.forEach(i -> {
                if(i.getStockReceiptItem().getId().equals(item.getId())){
                    i.setWprice(item.getWprice());
                    crudApi.save(i);
                }
                
            });
        });
    }
    public Inventory getInventory()
    {
        return inventory;
    }

    public void setInventory(Inventory inventory)
    {
        this.inventory = inventory;
    }

    public List<Inventory> getInventoryList()
    {
        return inventoryList;
    }

    public String getOptionText()
    {
        return optionText;
    }

    public List<StockReceiptItem> getStockReceiptItemList() {
        return stockReceiptItemList;
    }

    public StockReceiptItem getSelectedStockReceiptItem() {
        return selectedStockReceiptItem;
    }

    public void setSelectedStockReceiptItem(StockReceiptItem selectedStockReceiptItem) {
        this.selectedStockReceiptItem = selectedStockReceiptItem;
    }
    
    public UnitMeasurement getSelectedUnit()
    {
        return selectedUnit;
    }

    public List<Inventory> getSegmentedList()
    {
        return segmentedList;
    }
    
}
