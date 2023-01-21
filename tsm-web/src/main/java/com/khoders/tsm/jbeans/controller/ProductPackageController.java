
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.entities.Product;
import com.khoders.tsm.services.InventoryService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.ProductPackage;
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
@Named(value = "productPackageController")
@SessionScoped
public class ProductPackageController implements Serializable
{
   @Inject private CrudApi crudApi;
   @Inject private AppSession appSession;
   @Inject private InventoryService inventoryService;
   @Inject private StockService stockService;
   
   private ProductPackage productPackage;
   private List<ProductPackage> productPackageList = new LinkedList<>();
   private List<ProductPackage> segmentedList = new LinkedList<>();
   private List<Product> productList = new LinkedList<>();
   private String optionText;
   private Product selectedProduct = null;
   private UnitMeasurement selectedUnit = null;
   
   @PostConstruct
   private void init(){
     clearProductPackage();  
   }
   
   public void initProduct(){     
     productList = inventoryService.getProducts();
   }
   
   public void initProductPackage(){
     productPackageList = inventoryService.getProductPackageList();
   }
   
   public void selectProduct(Product product){
       selectedProduct=product;
       segmentedList = stockService.segmentedProducts(selectedProduct);
   }
   
   public void updateUnit(){
       selectedUnit = productPackage.getUnitMeasurement();
   }
   
   public void saveProductPackage()
   {
       if(selectedProduct == null){
           Msg.error("Please select a product");
           return;
       }
       try
       {
           if(optionText.equals("Save Changes")){
              ProductPackage newPackage = stockService.existProdctPackage(selectedProduct, productPackage.getUnitMeasurement());
              if (newPackage != null)
              {
                  Msg.error("product and the package already exist");
                  return;
              }
          }
           if(productPackage.getUnitsInPackage() == 0.0){
               Msg.error("Units in package is required");
               return;
           }
           if(productPackage.getPackagePrice() == 0.0){
               Msg.error("Selling price is required");
               return;
           }
          productPackage.genCode();
          productPackage.setProduct(selectedProduct);
          if(crudApi.save(productPackage) != null){
              productPackageList = CollectionList.washList(productPackageList, productPackage);
               Msg.info(Msg.SUCCESS_MESSAGE);
          }
          clearProductPackage();
       } catch (Exception e)
       {
          e.printStackTrace();
       }
   }
   
   public void deleteProductPackage(ProductPackage productPackage)
   {
       try
       {
         if(crudApi.delete(productPackage))
         {
             productPackageList.remove(productPackage);
             Msg.info(Msg.SUCCESS_MESSAGE);
         }  
       } catch (Exception e)
       {
         e.printStackTrace();
       }
   }
   
   public void editProductPackage(ProductPackage productPackage){
       this.productPackage = productPackage;
       selectedProduct = productPackage.getProduct();
       this.optionText = "Update";
   }

    public void clearProductPackage()
    {
        productPackage = new ProductPackage();
        productPackage.setUserAccount(appSession.getCurrentUser());
        selectedProduct = null;
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
    }

    public ProductPackage getProductPackage()
    {
        return productPackage;
    }

    public void setProductPackage(ProductPackage productPackage)
    {
        this.productPackage = productPackage;
    }

    public List<ProductPackage> getProductPackageList()
    {
        return productPackageList;
    }

    public String getOptionText()
    {
        return optionText;
    }

    public List<Product> getProductList()
    {
        return productList;
    }

    public Product getSelectedProduct()
    {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct)
    {
        this.selectedProduct = selectedProduct;
    }

    public UnitMeasurement getSelectedUnit()
    {
        return selectedUnit;
    }

    public List<ProductPackage> getSegmentedList()
    {
        return segmentedList;
    }
    
}
