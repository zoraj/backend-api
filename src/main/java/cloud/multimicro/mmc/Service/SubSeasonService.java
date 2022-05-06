/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.SubSeasonDao;
import cloud.multimicro.mmc.Entity.TPmsSousSaison;
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
@Path("sub-season")
@Produces(MediaType.APPLICATION_JSON)
public class SubSeasonService {
    private static final Logger LOGGER = Logger.getLogger(BookingService.class);
    @Inject
    SubSeasonDao seasonDao;
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSubSeason() {
        List<TPmsSousSaison> sousSaisonList = seasonDao.getAllSubSeason();
        if (sousSaisonList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(sousSaisonList, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/by-season/{season-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubSeasonBySeason(@PathParam("season-id") Integer id) {
        List<TPmsSousSaison> sousSaisonList = seasonDao.getSubSeasonBySeason(id);
        if (sousSaisonList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(sousSaisonList, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubSeasonById(@PathParam("id") int id) {
        TPmsSousSaison collectionMode = seasonDao.getSubSeasonById(id);        
        if (collectionMode == null) {
            throw new NotFoundException();
        }
        return Response.ok(collectionMode, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSubSeason(TPmsSousSaison sousSaison) {
        try {
            seasonDao.addSubSeason(sousSaison);
            return Response.status(Response.Status.CREATED).entity(sousSaison).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSubSeason(TPmsSousSaison sousSaison) {
        try {
            seasonDao.updateSubSeason(sousSaison);
            return Response.status(Response.Status.OK).entity(sousSaison).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSubSeason(@PathParam("id") int id) {
        try {
           seasonDao.deleteSubSeason(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
