/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.CleaningScheduleDao;
import cloud.multimicro.mmc.Entity.TPmsCalendrierNettoyage;
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
 * @author Naly
 */
@Stateless
@Path("cleaning-schedule")
@Produces(MediaType.APPLICATION_JSON)
public class CleaningScheduleService {
    private static final Logger LOGGER = Logger.getLogger(CleaningScheduleService.class);
    @Inject
    CleaningScheduleDao cleaningDao;
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TPmsCalendrierNettoyage> cleaning = cleaningDao.getAll();
        if (cleaning.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(cleaning, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) {
        TPmsCalendrierNettoyage cleaning = cleaningDao.getById(id);        
        if (cleaning == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(cleaning, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCleaningSchedule(TPmsCalendrierNettoyage cleaning) {
        try {
            cleaningDao.setPmsCalendrierNettoyage(cleaning);
            return Response.status(Response.Status.CREATED).entity(cleaning).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCleaningSchedule(TPmsCalendrierNettoyage pmsCalendrierNettoyage) {
        try {
            cleaningDao.updateCleaningSchedule(pmsCalendrierNettoyage);
            return Response.status(Response.Status.OK).entity(pmsCalendrierNettoyage).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCleaningSchedule(@PathParam("id") Integer id) {
        try {
           cleaningDao.deleteCleaningSchedule(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
