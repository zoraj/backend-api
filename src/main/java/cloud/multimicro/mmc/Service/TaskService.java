/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.TaskDao;
import cloud.multimicro.mmc.Entity.TMmcTache;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
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
@Path("tache")
@Produces(MediaType.APPLICATION_JSON)
public class TaskService {
    
    private static final Logger LOGGER = Logger.getLogger(ProductService.class);
    @Inject
    TaskDao taskDao;
    
    @GET
    @Path("/{module}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTache(@PathParam("module") String module) {
        List<TMmcTache> tache = taskDao.getTache(module);
        if (tache.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(tache, MediaType.APPLICATION_JSON).build();
    }
    
}
