/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.PlanningRecepDao;
import cloud.multimicro.mmc.Entity.VPmsEditionPlanningJour;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Naly
 */
@Stateless
@Path("planning")
@Produces(MediaType.APPLICATION_JSON)
public class PlanningRecepService {
    
    @Inject
    PlanningRecepDao planningRecepDao;
    
    @GET
    @Path("/recep-of-day")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlanningDay(@Context UriInfo info) {
        String dateDebut = info.getQueryParameters().getFirst("dateStart");
        List<VPmsEditionPlanningJour>  planningDay = planningRecepDao.getAllPlanningDay(dateDebut);
        if (planningDay.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(planningDay, MediaType.APPLICATION_JSON).build();
    }
    
}
