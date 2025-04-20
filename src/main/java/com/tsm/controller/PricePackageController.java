/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.dto.PricePackagingDto;
import com.tsm.services.ProductService;
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
@Path(ApiEndpoint.PRICE_PACKAGE_ENDPOINT)
public class PricePackageController {
    @Inject private ProductService productService;
 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
        return JaxResponse.ok(Msg.RECORD_FOUND, productService.findAllPrices());
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PricePackagingDto pack){
        PricePackagingDto dto = productService.save(pack);
        return JaxResponse.created(Msg.CREATED, dto);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(PricePackagingDto typeDto){
        PricePackagingDto dto = productService.save(typeDto);
        return JaxResponse.ok(Msg.UPDATED, dto);
    }
    
    @GET
    @Path("/{pricePackageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("pricePackageId") String pricePackageId){
        return JaxResponse.ok(Msg.RECORD_FOUND, productService.findPricePackageById(pricePackageId));
    }
    
    @GET
    @Path("/{inventoryId}/prices")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadPriceByInventoryId(@PathParam("inventoryId") String inventoryId){
        return JaxResponse.ok(Msg.RECORD_FOUND, productService.loadPricePackageByInventory(inventoryId));
    }
    
    @DELETE
    @Path("/{pricePackageId}")
    public Response delete(@PathParam("pricePackageId") String pricePackageId){
        boolean delete = productService.deletePricePackaging(pricePackageId);
        if(delete)
            return JaxResponse.ok(Msg.DELETE_MESSAGE,delete);
        return JaxResponse.ok("Could not delete product type",delete);
    }
}
