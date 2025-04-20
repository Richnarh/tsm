/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.lookups;


import com.dolphindoors.resource.enums.AccessLevel;
import com.dolphindoors.resource.enums.DeliveryStatus;
import com.dolphindoors.resource.enums.PaymentMethod;
import com.dolphindoors.resource.enums.PaymentStatus;
import com.dolphindoors.resource.enums.Status;
import com.dolphindoors.resource.enums.Title;
import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.utilities.Msg;
import com.lestieshop.lookups.LookupSetup;
import com.tsm.ApiEndpoint;
import com.tsm.enums.ClientSource;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Pascal
 */
@Path(ApiEndpoint.LOOKUP_ENDPOINT)
public class LookupController {
    @Inject private LookupService lookupService;
    
    @GET
    @Path("/payment-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response paymentStatus(){
        return JaxResponse.ok(Msg.RECORD_FOUND, LookupSetup.PrepareEnum(PaymentStatus.values()));
    }
    @GET
    @Path("/delivery-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deliveryStatus(){
        return JaxResponse.ok(Msg.RECORD_FOUND, LookupSetup.PrepareEnum(DeliveryStatus.values()));
    }
    @GET
    @Path("/payment-method")
    @Produces(MediaType.APPLICATION_JSON)
    public Response paymentMethod(){
        return JaxResponse.ok(Msg.RECORD_FOUND, LookupSetup.PrepareEnum(PaymentMethod.values()));
    }
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response status(){
        return JaxResponse.ok(Msg.RECORD_FOUND, LookupSetup.PrepareEnum(Status.values()));
    }
    @GET
    @Path("/access-level")
    @Produces(MediaType.APPLICATION_JSON)
    public Response accessLevel(){
        return JaxResponse.ok(Msg.RECORD_FOUND, LookupSetup.PrepareEnum(AccessLevel.values()));
    }
    @GET
    @Path("/client-source")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clientSource(){
        return JaxResponse.ok(Msg.RECORD_FOUND, LookupSetup.PrepareEnum(ClientSource.values()));
    }
    
    @GET
    @Path("/title")
    @Produces(MediaType.APPLICATION_JSON)
    public Response title(){
        return JaxResponse.ok(Msg.RECORD_FOUND, LookupSetup.PrepareEnum(Title.values()));
    }
    
    // Entities
    @GET
    @Path("/inventory")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stock(){
        return JaxResponse.ok(Msg.RECORD_FOUND, lookupService.inventory());
    }
    
    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response products(){
        return JaxResponse.ok(Msg.RECORD_FOUND, lookupService.products());
    }
    @GET
    @Path("/company-profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response companyProfile(){
        return JaxResponse.ok(Msg.RECORD_FOUND, lookupService.companyProfile());
    }
    
    @GET
    @Path("/product-type")
    @Produces(MediaType.APPLICATION_JSON)
    public Response productType(){
        return JaxResponse.ok(Msg.RECORD_FOUND, lookupService.productTypes());
    }
    @GET
    @Path("/employees")
    @Produces(MediaType.APPLICATION_JSON)
    public Response employees(){
        return JaxResponse.ok(Msg.RECORD_FOUND, lookupService.employees());
    }
    @GET
    @Path("/job-title")
    @Produces(MediaType.APPLICATION_JSON)
    public Response jobTitle(){
        return JaxResponse.ok(Msg.RECORD_FOUND, lookupService.jobTitles());
    }
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response users(){
        return JaxResponse.ok(Msg.RECORD_FOUND, lookupService.users());
    }
    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response customers(){
        return JaxResponse.ok(Msg.RECORD_FOUND, lookupService.customers());
    }
    @GET
    @Path("/packaging")
    @Produces(MediaType.APPLICATION_JSON)
    public Response packaging(){
        return JaxResponse.ok(Msg.RECORD_FOUND, lookupService.packaging());
    }
    
    @GET
    @Path("/price-package")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pricepPackage(){
        return JaxResponse.ok(Msg.RECORD_FOUND, lookupService.pricepPackage());
    }
    
}
