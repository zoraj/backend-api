/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.BankDao;
import cloud.multimicro.mmc.Entity.TMmcBanque;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Asynchronous;
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
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import static org.jboss.logging.Logger.Level.ERROR;

/**
 *
 * @author HERIZO
 */
@Stateless
@Path("banque")
@Produces(MediaType.APPLICATION_JSON)
public class BankService {
    private static final Logger LOGGER = Logger.getLogger(ClientService.class);
    @Inject
    BankDao BankDao;
    
    //banque
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TMmcBanque> banque = BankDao.getAll();
        if (banque.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(banque, MediaType.APPLICATION_JSON).build();       
    }
   
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) {
        TMmcBanque banque = BankDao.getByIdBanque(id);        
        if (banque == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(banque, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBanque(TMmcBanque banque) {
        try {
            BankDao.updateBanque(banque);
            return Response.status(Response.Status.OK).entity(banque).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBanque(@PathParam("id") int id) {
        try {
           BankDao.deleteBanque(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }  
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setBanque(TMmcBanque banque) {
        try {
            BankDao.setBanque(banque);
            return Response.status(Response.Status.CREATED).entity(banque).build();
        } catch (CustomConstraintViolationException  e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    
    
    
}
