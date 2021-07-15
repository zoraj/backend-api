/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.ServiceDao;
import cloud.multimicro.mmc.Entity.TMmcService;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
 * @author Naly-PC
 */
@Stateless
@Path("services")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceService {
    
    private static final Logger LOGGER = Logger.getLogger(ServiceService.class);
    @Inject
    ServiceDao serviceDao;
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllService() {
        List<TMmcService> service = serviceDao.getAllService();
        if (service.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(service, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByIdSociete(@PathParam("id") int id) {
        TMmcService service = serviceDao.getByIdService(id);        
        if (service == null) {
            throw new NotFoundException();  
        }
        return Response.ok(service, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addService(TMmcService service) {
        try {
            serviceDao.setService(service);
            return Response.status(Response.Status.CREATED).entity(service).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateService(TMmcService service) {
        try {
            serviceDao.updateService(service);
            return Response.status(Response.Status.OK).entity(service).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
}
