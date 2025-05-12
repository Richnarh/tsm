/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.jpa.CrudApi;
import com.dolphindoors.resource.utilities.JUtils;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.dto.AuthRequest;
import com.tsm.dto.UserDto;
import com.tsm.entities.Employee;
import com.tsm.entities.system.UserAccount;
import com.tsm.mapper.EmployeeMapper;
import com.tsm.services.EmployeeService;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richard
 */
@Path(ApiEndpoint.AUTH_ENDPOINT)
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Inject
    private CrudApi crudApi;
    @Inject private EmployeeMapper mapper;
    @Inject private EmployeeService es;

    @POST
    @Path(value = "/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doLogin(AuthRequest authRequest) {
        log.info("Signing in...");
        Employee employee = es.getEmployee(authRequest.getEmailAddress());
        String password = JUtils.hashPassword(authRequest.getPassword().toCharArray(), employee.getSalt());
        UserAccount userAccount = crudApi.getEm().createQuery("SELECT e FROM UserAccount e WHERE e.employee =:employee AND e.password=:password", UserAccount.class)
                    .setParameter(UserAccount._employee, employee)
                    .setParameter(UserAccount._password, password)
                    .getResultStream().findFirst().orElse(null);
        
        if (userAccount != null) {
            UserDto userDto = mapper.toDto(userAccount);
            return JaxResponse.ok(Msg.RECORD_FOUND, userDto);
        }
        return JaxResponse.error(Msg.FAILED, "Email/Password Incorrect");
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(UserDto dto){
        UserDto user = es.updateUser(dto);
        return JaxResponse.ok("Account created successfully.", user);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(UserDto dto){
        UserDto user = es.updateUser(dto);
        return JaxResponse.ok("Account updated successfully.", user);
    }
}
