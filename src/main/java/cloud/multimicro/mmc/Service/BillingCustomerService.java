/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.BillingCustomerDao;
import cloud.multimicro.mmc.Entity.TMmcClientFacturation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("billing-customer")
@Produces(MediaType.APPLICATION_JSON)
public class BillingCustomerService {

    private static final Logger LOGGER = Logger.getLogger(BookingService.class);
    @Inject
    BillingCustomerDao billingCustomerDao;
    
    @GET
    @Path("/")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TMmcClientFacturation> billingCustomer = billingCustomerDao.getAll();
        if (billingCustomer.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(billingCustomer, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByIdClient(@PathParam("id") Integer id) {
        TMmcClientFacturation billingCustomer = billingCustomerDao.getByIdClient(id);        
        if (billingCustomer == null) {
            throw new NotFoundException();
        }
        return Response.ok(billingCustomer, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(TMmcClientFacturation billingCustomer) {
        try {
            billingCustomerDao.add(billingCustomer);
            return Response.status(Response.Status.CREATED).entity(billingCustomer).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(TMmcClientFacturation billingCustomer) {
        try {
            billingCustomerDao.update(billingCustomer);
            return Response.status(Response.Status.OK).entity(billingCustomer).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
      
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        try {
           billingCustomerDao.delete(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }
    

}
