/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsm.controller;

import com.dolphindoors.resource.jaxrs.JaxResponse;
import com.dolphindoors.resource.jpa.CrudApi;
import static com.dolphindoors.resource.utilities.JUtils.hashText;
import com.dolphindoors.resource.utilities.Msg;
import com.tsm.ApiEndpoint;
import com.tsm.dto.AuthRequest;
import com.tsm.dto.UserDto;
import com.tsm.entities.system.UserAccount;
import com.tsm.mapper.UserMapper;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richa
 */
@Path(ApiEndpoint.AUTH_ENDPOINT)
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Inject
    private CrudApi crudApi;
    @Inject private UserMapper mapper;

    @POST
    @Path(value = "/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doLogin(AuthRequest authRequest) {
        log.info("Signing in...");
        
        UserAccount userAccount = crudApi.getEm().createQuery("SELECT e FROM UserAccount e WHERE e.emailAddress= :emailAddress AND e.password=:password", UserAccount.class)
                    .setParameter(UserAccount._emailAddress, authRequest.getEmailAddress())
                    .setParameter(UserAccount._password, hashText(authRequest.getPassword()))
                    .getResultStream().findFirst().orElse(null);
        
        UserDto userDto = mapper.toDto(userAccount);
        if (userDto != null) {
            return JaxResponse.ok(Msg.RECORD_FOUND, userDto);
        }
        return JaxResponse.error(Msg.FAILED, "Email/Password Incorrect");
    }
}
