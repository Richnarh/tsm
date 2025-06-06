
package com.tsm.jbeans.controller;

import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.entities.Location;
import com.tsm.services.InventoryService;
import com.tsm.listener.AppSession;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SystemUtils;

/**
 *
 * @author richa
 */
@Named(value = "locationController")
@SessionScoped
public class LocationController implements Serializable{
   @Inject private CrudApi crudApi;
   @Inject private AppSession appSession;
   @Inject private InventoryService inventoryService;
   
   private Location location = new Location();
   private List<Location> locationList = new LinkedList<>();
   private String optionText;
   
   @PostConstruct
   private void init(){
     clearLocation();
     locationList = inventoryService.getLocationList();
   }
   
   public void saveLocation(){
       try
       {
          location.genCode();
          if(crudApi.save(location) != null)
          {
              locationList = JUtils.addToList(locationList, location);
              Msg.info(Msg.SUCCESS_MESSAGE);
          }
          clearLocation();
       } catch (Exception e)
       {
          e.printStackTrace();
       }
   }
   
   public void deleteLocation(Location location){
       try
       {
         if(crudApi.delete(location))
         {
             locationList.remove(location);
             Msg.info(Msg.SUCCESS_MESSAGE);
         }  
       } catch (Exception e)
       {
         e.printStackTrace();
       }
   }
   
   public void editLocation(Location location){
       this.location = location;
       optionText = "Update";
   }

    public void clearLocation()
    {
        location = new Location();
        location.setUserAccount(appSession.getCurrentUser());
        location.setCompanyBranch(appSession.getCompanyBranch());
        optionText = "Save Changes";
        JUtils.resetJsfUI();
    }

    public Location getLocation()
    {
      return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public List<Location> getLocationList()
    {
        return locationList;
    }

    public String getOptionText()
    {
        return optionText;
    }
}
