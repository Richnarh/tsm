/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.dto.InventoryDto;
import com.tsm.dto.SalesInventory;
import com.tsm.services.ProductService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Pascal
 */
@Path(ApiEndpoint.INVENTORY_ENDPOINT)
public class InventoryController {
    @Inject private CrudApi crudApi;
    @Inject private ProductService productService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(InventoryDto inventoryDto){
        InventoryDto dto = productService.save(inventoryDto);
        if(dto != null)
            return JaxResponse.created(Msg.CREATED, dto);
        return JaxResponse.error(Msg.FAILED, "Could not save inventory");
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(InventoryDto inventoryDto){
        InventoryDto dto = productService.save(inventoryDto);
        return JaxResponse.ok(Msg.UPDATED, dto);
    }
    
    @GET
    @Path("/{inventoryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("inventoryId") String inventoryId){
        return JaxResponse.ok(Msg.RECORD_FOUND, productService.findById(inventoryId));
    }
    
    @GET
    @Path("/packages/{inventoryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadPackages(@PathParam("inventoryId") String inventoryId){
        return JaxResponse.ok(Msg.RECORD_FOUND, productService.loadPackages(inventoryId));
    }
    
    @GET
    @Path("/inventory-list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
        return JaxResponse.ok(Msg.RECORD_FOUND, productService.findAll());
    }
    
    @DELETE
    @Path("/{inventoryId}")
    public Response delete(@PathParam("inventoryId") String inventoryId){
        boolean delete = productService.deleteInventory(inventoryId);
        if(delete)
            return JaxResponse.ok(Msg.DELETE_MESSAGE,delete);
        return JaxResponse.ok("Could not delete inventory",delete);
    }
    
    @GET
    @Path("/inventory-sales")
    public Response sales(){
        List<SalesInventory> inventory = productService.getSaleInventories();
        return JaxResponse.ok(inventory);
    }
}
