package ar.edu.unnoba.pdyc.mymusic.resource;

import ar.edu.unnoba.pdyc.mymusic.dto.AuthenticationRequestDTO;
import ar.edu.unnoba.pdyc.mymusic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
*
*   POST http://localhost:8080/mymusic/app/signup                  registro de usuario
*
* */
@Path("/signup")
public class UserResource {

    @Autowired
    private UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void NewUser(@Suspended AsyncResponse response, AuthenticationRequestDTO authDto){
        this.userService.newUserAsync(authDto.getEmail(), authDto.getPassword()).handle((res,ex) -> {
            if(res != null){
                response.resume(Response.status(Response.Status.CREATED).build());
            }
            if(ex.getCause() instanceof DataIntegrityViolationException){
                response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            }
            return null;
        });
    }
}