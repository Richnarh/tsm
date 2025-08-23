/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.AppParam;
import com.tsm.dto.CreditPaymentDto;
import com.tsm.services.PaymentService;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
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
@Path(ApiEndpoint.CREDIT_PAYMENT)
public class CreditPaymentController {
    @Inject
    private PaymentService ps;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@BeanParam AppParam param, CreditPaymentDto paymentDto){
        CreditPaymentDto dto = ps.save(paymentDto,param);
        return JaxResponse.created(Msg.CREATED, dto);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@BeanParam AppParam param, CreditPaymentDto paymentDto){
        CreditPaymentDto dto = ps.save(paymentDto,param);
        return JaxResponse.created(Msg.CREATED, dto);
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") String id){
        return JaxResponse.ok(Msg.RECORD_FOUND, ps.findById(id));
    }
    
    @GET
    @Path("/{salesId}/sales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCreditsByInvoiceId(@PathParam("salesId") String salesId){
        return JaxResponse.ok(Msg.RECORD_FOUND, ps.getCreditPaymentsByInvoice(salesId));
    }
    
    @GET
    @Path("/report/{invoiceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response report(@PathParam("invoiceId") String invoiceId){
//        byte[] rpt = ps.creditReport(invoiceId);
//        return JaxResponse.ok(Msg.RECORD_FOUND, Base64.getEncoder().encodeToString(rpt));
          return null;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
        return JaxResponse.ok(Msg.RECORD_FOUND, ps.findAllCreditPayments());
    }
    
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id){
        boolean delete = ps.deleteCreditPayment(id);
        if(delete)
            return JaxResponse.ok(Msg.DELETE_MESSAGE,delete);
        return JaxResponse.ok("Could not delete credit paymnent",delete);
    }
}
