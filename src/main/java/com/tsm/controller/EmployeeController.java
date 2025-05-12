/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.dto.EmployeeDto;
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
@Path(ApiEndpoint.EMPLOYEE_ENDPOINT)
public class EmployeeController {
    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    @Inject private EmployeeService employeeService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(EmployeeDto dto){
        EmployeeDto employeeDto = employeeService.save(dto);
        return JaxResponse.created(Msg.CREATED, employeeDto);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(EmployeeDto dto){
        EmployeeDto employeeDto = employeeService.save(dto);
        return JaxResponse.ok(Msg.UPDATED, employeeDto);
    }
    
    @GET
    @Path("/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("employeeId") String employeeId){
        EmployeeDto employeeDto = employeeService.findById(employeeId);
        return JaxResponse.ok(Msg.RECORD_FOUND, employeeDto);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
         return JaxResponse.ok(employeeService.fetchEmployees());
    }
    
    @DELETE
    @Path("/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("employeeId") String employeeId){
        boolean delete = employeeService.deleteEmployee(employeeId);
        return JaxResponse.ok(Msg.DELETED, delete);
    }
    
    @GET
    @Path("/attendance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response attendancex(){
    // return JaxResponse.ok(employeeService.fetchAttendance());
        return null;
    }
}
