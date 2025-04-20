/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.dto.AppConfigDto;
import com.tsm.services.AppConfigService;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Pascal
 */
@Path(ApiEndpoint.APP_CONFIG_ENDPOINT)
public class AppConfigController {
    @Inject private AppConfigService configService;
    
    @POST
    public Response create(AppConfigDto dto){ 
        AppConfigDto configDto = configService.save(dto);
        return JaxResponse.created(Msg.CREATED, configDto);
    }
    
    @PUT
    public Response update(AppConfigDto dto){ 
        AppConfigDto configDto = configService.save(dto);
        return JaxResponse.ok(Msg.UPDATED, configDto);
    }
    @PUT
    @Path("/{configName}/{configValue}")
    public Response updateByConfigName(@PathParam("configName") String configName, @PathParam("configValue") String configValue){ 
        AppConfigDto configDto = configService.update(configName,configValue);
        return JaxResponse.ok(Msg.UPDATED, configDto);
    }
    
    @GET
    @Path("/{configId}")
    public Response findById(@PathParam("configId") String configId){
        AppConfigDto dto = configService.findById(configId);
        return JaxResponse.ok(Msg.RECORD_FOUND,dto);
    }
    
    @GET
    @Path("/gen-code")
    public Response genCode(){
        return JaxResponse.ok(JUtils.generate(5).toUpperCase());
    }
    
    @GET
    @Path("/{configName}/config")
    public Response findByConfigName(@PathParam("configName") String configName){
        AppConfigDto dto = configService.findByConfigName(configName);
        return JaxResponse.ok(Msg.RECORD_FOUND,dto);
    }
    
    @GET
    @Path("/config-list")
    public Response findAll(){
        return JaxResponse.ok(configService.fetchAllConfigs());
    }
    
    @DELETE
    @Path("/{configId}")
    public Response delete(@PathParam("configId") String configId){
        boolean delete = configService.delete(configId);
        if(delete)
            return JaxResponse.ok(Msg.DELETE_MESSAGE,delete);
        return JaxResponse.ok("Could not delete config",delete);
    }
}
