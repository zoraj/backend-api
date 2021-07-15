/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.PlanningDao;
import cloud.multimicro.mmc.Dao.RoomDao;
import cloud.multimicro.mmc.Entity.VPmsEditionPlanningMensuelChambre;
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
public class PlanningService {
    
    @Inject
    PlanningDao palnningDao;
    
    //Planning mensuel chambre
    @GET
    @Path("/planning-mensuel-room")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlanningRoomMonth(@Context UriInfo info) {
        String dateDebut = info.getQueryParameters().getFirst("dateStart");
        List<VPmsEditionPlanningMensuelChambre>  planningMonth = palnningDao.getAllPlanningRoomMonth(dateDebut);
        if (planningMonth.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(planningMonth, MediaType.APPLICATION_JSON).build();
    }
    
}
