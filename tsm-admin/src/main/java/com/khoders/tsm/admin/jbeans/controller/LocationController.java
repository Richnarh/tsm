
package com.khoders.tsm.admin.jbeans.controller;

import com.khoders.tsm.entities.Location;
import com.khoders.resource.jpa.CrudApi;
import com.khoders.resource.utilities.CollectionList;
import com.khoders.resource.utilities.Msg;
import com.khoders.resource.utilities.SystemUtils;
import com.khoders.tsm.admin.listener.AppSession;
import com.khoders.tsm.admin.services.StockService;
import com.khoders.tsm.entities.system.CompanyBranch;
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
   @Inject private AppSession appSession;
   @Inject private  StockService stockService;
   
   private Location location = new Location();
   private List<Location> locationList = new LinkedList<>();
   private String optionText;
   
   @PostConstruct
   private void init(){
     clearLocation();
     
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
       }
   }
   public void selectBranch(){
       if(location != null && location.getCompanyBranch() == null){
           Msg.error("Something went wrong loading locations based on selected warehouse");
           return;
       }
       locationList = stockService.getLocationList(location.getCompanyBranch());
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
