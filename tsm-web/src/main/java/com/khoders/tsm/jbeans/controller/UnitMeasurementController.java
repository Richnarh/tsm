
package com.khoders.tsm.jbeans.controller;

import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.entities.UnitMeasurement;
import com.khoders.tsm.listener.AppSession;
import com.khoders.tsm.services.InventoryService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author richa
 */
@Named(value = "unitMeasurementController")
@SessionScoped
public class UnitMeasurementController implements Serializable
{
   @Inject private CrudApi crudApi;
   @Inject private AppSession appSession;
   @Inject private InventoryService inventoryService;
   
   private UnitMeasurement unitMeasurement;
   private List<UnitMeasurement> unitMeasurementList = new LinkedList<>();
   private String optionText;
   
   @PostConstruct
   private void init()
   {
     clearUnitMeasurement();
     unitMeasurementList = inventoryService.getUnitMeasurementList();
   }
   
   public void saveUnitMeasurement()
   {
       try
       {
          unitMeasurement.genCode();
          if(crudApi.save(unitMeasurement) != null){
              unitMeasurementList = CollectionList.washList(unitMeasurementList, unitMeasurement);
              
              FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.SUCCESS_MESSAGE, null));
          }
          clearUnitMeasurement();
       } catch (Exception e)
       {
          e.printStackTrace();
       }
   }
   
   public void deleteUnitMeasurement(UnitMeasurement unitMeasurement){
       try
       {
         if(crudApi.delete(unitMeasurement))
         {
             unitMeasurementList.remove(unitMeasurement);
             
             FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage(FacesMessage.SEVERITY_INFO, Msg.SUCCESS_MESSAGE, null));
         }  
       } catch (Exception e)
       {
         e.printStackTrace();
       }
   }
   
   public void editUnitMeasurement(UnitMeasurement unitMeasurement){
       this.unitMeasurement = unitMeasurement;
   }

    public void clearUnitMeasurement()
    {
        unitMeasurement = new UnitMeasurement();
        unitMeasurement.setUserAccount(appSession.getCurrentUser());
        unitMeasurement.setCompanyBranch(appSession.getCompanyBranch());
        unitMeasurement.genCode();
        optionText = "Save";
        SystemUtils.resetJsfUI();
    }

    public UnitMeasurement getUnitMeasurement()
    {
        return unitMeasurement;
    }

    public void setUnitMeasurement(UnitMeasurement unitMeasurement)
    {
        this.unitMeasurement = unitMeasurement;
    }

    public List<UnitMeasurement> getUnitMeasurementList()
    {
        return unitMeasurementList;
    }

    public String getOptionText()
    {
        return optionText;
    }
    
}
