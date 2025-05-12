/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.dto.CompanyBranchDto;
import com.tsm.services.CompanyService;
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
@Path(ApiEndpoint.COMPANY_BRANCH_ENDPOINT)
public class CompanyBranchController {
    @Inject private CompanyService cs;
 
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CompanyBranchDto typeDto){
        CompanyBranchDto dto = cs.save(typeDto);
        return JaxResponse.created(Msg.CREATED, dto);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(CompanyBranchDto typeDto){
        CompanyBranchDto dto = cs.save(typeDto);
        return JaxResponse.ok(Msg.UPDATED, dto);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
        return JaxResponse.ok(Msg.RECORD_FOUND, cs.fetchCompanyBranches());
    }
    
    @GET
    @Path("/{companyBranchId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("companyBranchId") String companyBranchId){
        return JaxResponse.ok(Msg.RECORD_FOUND, cs.findById(companyBranchId));
    }
    
    @DELETE
    @Path("/{companyBranchId}")
    public Response delete(@PathParam("companyBranchId") String companyBranchId){
        boolean delete = cs.deleteBranch(companyBranchId);
        if(delete)
            return JaxResponse.ok(Msg.DELETE_MESSAGE,delete);
        return JaxResponse.ok("Could not delete product type",delete);
    }
}
