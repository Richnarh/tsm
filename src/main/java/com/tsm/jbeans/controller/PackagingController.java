
package com.tsm.jbeans.controller;

import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.services.InventoryService;
import com.tsm.entities.PricePackage;
import com.tsm.listener.AppSession;
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
@Named(value = "packagingController")
@SessionScoped
public class PackagingController implements Serializable
{
   @Inject private AppSession appSession;
   @Inject private CrudApi crudApi;
   @Inject private InventoryService inventoryService;
   
   private PricePackage packaging = new PricePackage();
   private List<PricePackage> packagingList = new LinkedList<>();
   private String optionText;
   
   @PostConstruct
   private void init()
   {
     clearPackaging();
     packagingList = inventoryService.getPackagingList();
   }
   
   public void savePackaging()
   {
       try
       {
          if(crudApi.save(packaging) != null)
          {
              packagingList = JUtils.addToList(packagingList, packaging);
              Msg.info(Msg.SUCCESS_MESSAGE);
          }
          clearPackaging();
       } catch (Exception e)
       {
          e.printStackTrace();
       }
   }
   
   public void deletePackaging(PricePackage packaging){
       try
       {
         if(crudApi.delete(packaging))
         {
             packagingList.remove(packaging);
             Msg.info(Msg.SUCCESS_MESSAGE);
         }  
       } catch (Exception e)
       {
         e.printStackTrace();
       }
   }
   
   public void editPackaging(PricePackage packaging){
       this.packaging = packaging;
       optionText = "Update";
   }

    public void clearPackaging()
    {
        packaging = new PricePackage();
//        packaging.setUserAccount(appSession.getCurrentUser());
//        packaging.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        JUtils.resetJsfUI();
    }

    public PricePackage getPackaging()
    {
      return packaging;
    }

    public void setPackaging(PricePackage packaging)
    {
        this.packaging = packaging;
    }

    public List<PricePackage> getPackagingList()
    {
        return packagingList;
    }

    public String getOptionText()
    {
        return optionText;
    }
}
