/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.controller;
import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.dto.JobTitleDto;
import com.tsm.services.EmployeeService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Richard Narh
 */
@Path(ApiEndpoint.JOB_TITLE_ENDPOINT)
public class JobTitleController {
    private static final Logger log = LoggerFactory.getLogger(JobTitleController.class);
    @Inject private EmployeeService as;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(JobTitleDto dto){
        JobTitleDto jobTitleDto = as.save(dto);
        return JaxResponse.created(Msg.CREATED, jobTitleDto);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
         return JaxResponse.ok(as.fetchJobTitles());
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(JobTitleDto dto){
        JobTitleDto jobTitleDto = as.save(dto);
        return JaxResponse.ok(Msg.UPDATED, jobTitleDto);
    }
    
    @GET
    @Path("/{jobTitleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("jobTitleId") String jobTitleId){
        JobTitleDto jobTitleDto = as.findJobTitleById(jobTitleId);
        return JaxResponse.ok(Msg.RECORD_FOUND, jobTitleDto);
    }
    
    @DELETE
    @Path("/{jobTitleId}")
    public Response delete(@PathParam("jobTitleId") String jobTitleId){
        boolean delete = as.deleteTitle(jobTitleId);
        if(delete)
            return JaxResponse.ok(Msg.DELETE_MESSAGE,delete);
        return JaxResponse.ok("Could not delete JobTitle",delete);
    }
}
