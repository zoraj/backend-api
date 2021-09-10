/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */

package cloud.multimicro.mmc.Service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import cloud.multimicro.mmc.Dao.UserDao;
import cloud.multimicro.mmc.Entity.TMmcUser;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Util.Jwt;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

/**
 * UserService
 */
@Stateless
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    @Inject
    UserDao userDao;
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TMmcUser> users = userDao.getUsers();
        if (users.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(users, MediaType.APPLICATION_JSON).build();       
    }
    
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(TMmcUser user) {
        try {
            TMmcUser newUser = userDao.create(user);
            return Response.status(Response.Status.CREATED).entity(newUser).build();                          
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build(); 
        }
    }
    
    @PUT
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(TMmcUser user) {
        try {
            userDao.update(user);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        try {
           userDao.delete(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/signin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkCredentials(TMmcUser u) throws AuthenticationException {
        try{
           //String login = u.getLogin();
        String pinCode = u.getPinCode();

        TMmcUser user = userDao.checkCredentials(pinCode);
        if (user == null) {
            throw new AuthenticationException();
        }
        // Return a JWT token if everything is ok
        String token = Jwt.generateToken();
        user.setToken(token);
        return Response.ok(user, MediaType.APPLICATION_JSON).build();   
        }
        catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        
    }
}