/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.ServerCalendarDao;
import cloud.multimicro.mmc.Entity.TMmcUser;
import cloud.multimicro.mmc.Entity.TPosCalendrierServeur;
import cloud.multimicro.mmc.Entity.WaiterCalendarDashboard;
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

/**
 *
 * @author Naly
 */
@Stateless
@Path("calendar-server")
@Produces(MediaType.APPLICATION_JSON)
public class ServerCalendarService {
    @Inject
    ServerCalendarDao calendarDao;
    
    // GET ALL
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TPosCalendrierServeur> cServeur = calendarDao.getAll();
        if (cServeur.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(cServeur, MediaType.APPLICATION_JSON).build(); 
    }

    // GET ALL
    @Path("/calandar/{nbMonth}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCalendar(@PathParam("nbMonth") int nbMonth) {
        List<WaiterCalendarDashboard> cServeur = calendarDao.getCalendar(nbMonth);
        if (cServeur.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(cServeur, MediaType.APPLICATION_JSON).build();
    }
    
    // GET ALL
    @Path("/waiter")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllWaiter() {
        List<TMmcUser> cServeur = calendarDao.getServeur();
        if (cServeur.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(cServeur, MediaType.APPLICATION_JSON).build(); 
    }
    
    
    // GET BY ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) {
        TPosCalendrierServeur cServeur = calendarDao.getCalendrierServeurById(id);        
        if (cServeur == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(cServeur, MediaType.APPLICATION_JSON).build();   
    }
    
    // ADD
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCalendrierServeur(TPosCalendrierServeur cServeur) {
        try {
            calendarDao.setCalendrierServeur(cServeur);
            return Response.status(Response.Status.CREATED).entity(cServeur).build();
        } 
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    // UPDATE
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCalendrierServeur(TPosCalendrierServeur cServeur) {
        try {
            calendarDao.updateCalendrierServeur(cServeur);
            return Response.status(Response.Status.OK).entity(cServeur).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    // DELETE
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCalendrierServeur(@PathParam("id") int id) {
        try {
           calendarDao.deleteCalendrierServeur(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
