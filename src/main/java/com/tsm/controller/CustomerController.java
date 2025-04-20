/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.dto.CustomerDto;
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
@Path(ApiEndpoint.CUSTOMER_ENDPOINT)
public class CustomerController {
    @Inject private ProductService productService;
 
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CustomerDto typeDto){
        CustomerDto dto = productService.save(typeDto);
        return JaxResponse.created(Msg.CREATED, dto);
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(CustomerDto typeDto){
        CustomerDto dto = productService.save(typeDto);
        return JaxResponse.ok(Msg.UPDATED, dto);
    }
    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("customerId") String customerId){
        return JaxResponse.ok(Msg.RECORD_FOUND, productService.findCustomerById(customerId));
    }
    
    @GET
    @Path("/customer-list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
        return JaxResponse.ok(Msg.RECORD_FOUND, productService.fetchAllCustomers());
    }
    @DELETE
    @Path("/{customerId}")
    public Response delete(@PathParam("customerId") String customerId){
        boolean delete = productService.deleteCustomer(customerId);
        if(delete)
            return JaxResponse.ok(Msg.DELETE_MESSAGE,delete);
        return JaxResponse.ok("Could not delete customer",delete);
    }
}
