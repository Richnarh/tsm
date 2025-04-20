
package com.tsm.jbeans.controller;

import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.entities.UnitMeasurement;
import com.tsm.listener.AppSession;
import com.tsm.services.InventoryService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SystemUtils;

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
              unitMeasurementList = JUtils.addToList(unitMeasurementList, unitMeasurement);
              
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
        JUtils.resetJsfUI();
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
