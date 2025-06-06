
package com.tsm.jbeans.controller;

import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.services.InventoryService;
import com.tsm.entities.Inventory;
import com.tsm.entities.Product;
import com.tsm.entities.UnitMeasurement;
import com.tsm.listener.AppSession;
import com.tsm.services.StockService;
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
   private List<Product> productList = new LinkedList<>();
   private String optionText;
   private Product product = null;
   private UnitMeasurement selectedUnit = null;
   
   @PostConstruct
   private void init(){
     clearInventory();  
   }
   
   public void initProduct(){     
     productList = inventoryService.getProducts();
   }
   
   public void initInventory(){
     inventoryList = inventoryService.getInventoryList();
   }
   public void resetPage(){
       inventoryList = new LinkedList<>();
       segmentedList = new LinkedList<>();
       productList = new LinkedList<>();
       clearInventory();
   }
   public void selectProduct(Product product){
       this.product=product;
       segmentedList = stockService.inventoryProduct(product);
   }
   
   public void updateUnit(){
       selectedUnit = null;
   }
   
   public void saveInventory(){
       if(product == null){
           Msg.error("Please select a product");
           return;
       }
       try
       {
           if(optionText.equals("Save Changes")){
//              Inventory newPackage = stockService.getProduct(product, inventory.getUnitMeasurement());
//              if (newPackage != null){
//                  Msg.error("product and the package already exist");
//                  return;
//              }
          }
           
           if(inventory != null){
               Msg.error("Units in package is required");
               return;
           }
           if(inventory != null){
               Msg.error("Selling price is required");
               return;
           }
          inventory.genCode();
          inventory.setProduct(product);
          if(crudApi.save(inventory) != null){
              inventoryList = JUtils.addToList(inventoryList, inventory);
               Msg.info(Msg.SUCCESS_MESSAGE);
          }
          clearInventory();
       } catch (Exception e)
       {
       }
   }
   
   public void deleteInventory(Inventory inventory){
       try
       {
         if(crudApi.delete(inventory))
         {
             inventoryList.remove(inventory);
             Msg.info(Msg.SUCCESS_MESSAGE);
         }  
       } catch (Exception e)
       {
       }
   }
   
   public void editInventory(Inventory inventory){
       this.inventory = inventory;
       product = inventory.getProduct();
       this.optionText = "Update";
   }

    public void clearInventory()
    {
        inventory = new Inventory();
        inventory.setUserAccount(appSession.getCurrentUser());
        inventory.setCompanyBranch(appSession.getCompanyBranch());
        product = null;
        optionText = "Save Changes";
        JUtils.resetJsfUI();
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

    public UnitMeasurement getSelectedUnit()
    {
        return selectedUnit;
    }

    public List<Inventory> getSegmentedList()
    {
        return segmentedList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }
    
}
