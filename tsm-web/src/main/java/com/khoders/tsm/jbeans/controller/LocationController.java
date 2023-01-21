
package com.khoders.tsm.jbeans.controller;

import com.khoders.tsm.entities.Location;
import com.khoders.tsm.services.InventoryService;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
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
@Named(value = "locationController")
@SessionScoped
public class LocationController implements Serializable{
   @Inject private CrudApi crudApi;
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
              locationList = CollectionList.washList(locationList, location);
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
        optionText = "Save Changes";
        SystemUtils.resetJsfUI();
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
