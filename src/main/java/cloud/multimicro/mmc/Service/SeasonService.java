/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Service;
import cloud.multimicro.mmc.Dao.SeasonDao;
import cloud.multimicro.mmc.Entity.TPmsSaison;
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
 * BookingService
 */
@Stateless
@Path("seasons")
@Produces(MediaType.APPLICATION_JSON)
public class SeasonService {
    private static final Logger LOGGER = Logger.getLogger(BookingService.class);
    @Inject
    SeasonDao seasonDao;
    
    @GET
    @Path("/")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSeason() {
        List<TPmsSaison> saison = seasonDao.getAllSeason();
        if (saison.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(saison, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSeasonById(@PathParam("id") int id) {
        TPmsSaison saison = seasonDao.getSeasonById(id);        
        if (saison == null) {
            throw new NotFoundException();
        }
        return Response.ok(saison, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSeason(TPmsSaison saison) {
        try {
            seasonDao.addSeason(saison);
            return Response.status(Response.Status.CREATED).entity(saison).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSeason(TPmsSaison saison) {
        try {
            seasonDao.updateSeason(saison);
            return Response.status(Response.Status.OK).entity(saison).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSeason(@PathParam("id") int saisonId) {
        try {
           seasonDao.deleteSeason(saisonId);
           return Response.ok(saisonId, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }
}





