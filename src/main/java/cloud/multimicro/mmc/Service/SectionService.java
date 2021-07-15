/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.SectionDao;
import cloud.multimicro.mmc.Entity.TPosActivite;
import cloud.multimicro.mmc.Entity.TMmcPoste;
import cloud.multimicro.mmc.Entity.TMmcSite;
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
@Path("sections")
@Produces(MediaType.APPLICATION_JSON)
public class SectionService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class);
    @Inject
    SectionDao sectionDao;
    
    //ACTIVITE
    @GET
    @Path("/activities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TPosActivite> activities = sectionDao.getAll();
        if (activities.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(activities, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/activities/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivityById(@PathParam("id") int id) {
        TPosActivite activities = sectionDao.getActivityById(id);        
        if (activities == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(activities, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/activities")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPosActivity(TPosActivite activity) {
        try {
            sectionDao.setPosActivite(activity);
            return Response.status(Response.Status.CREATED).entity(activity).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/activities")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePosActivity(TPosActivite activity) {
        try {
            sectionDao.updatePosActivite(activity);
            return Response.status(Response.Status.OK).entity(activity).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/activities/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePosActivite(@PathParam("id") int id) {
        try {
           sectionDao.deletePosActivite(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
