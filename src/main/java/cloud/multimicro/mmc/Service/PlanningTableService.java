/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.PlanningTableDao;
import cloud.multimicro.mmc.Entity.TPosTablePlan;
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
 * @author Tsiory-PC
 */
@Stateless
@Path("seating-plan")
@Produces(MediaType.APPLICATION_JSON)
public class PlanningTableService {
    
    @Inject
    PlanningTableDao planningTableDao;
    
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSeatingPlan() {
        List<TPosTablePlan> tablePlan = planningTableDao.getSeatingPlan();
        if (tablePlan.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(tablePlan, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSeatingPlanById(@PathParam("id") int id) {
        TPosTablePlan tablePlan = planningTableDao.getSeatingPlanById(id);
        if (tablePlan == null) {
            throw new NotFoundException();
        }
        return Response.ok(tablePlan, MediaType.APPLICATION_JSON).build();
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSeatingPlan(TPosTablePlan tablePlan) {
        try {
            planningTableDao.setSeatingPlan(tablePlan);
            return Response.status(Response.Status.CREATED).entity(tablePlan).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSeatingPlan(TPosTablePlan tablePlan) {
        try {
            planningTableDao.updateSeatingPlan(tablePlan);
            return Response.status(Response.Status.OK).entity(tablePlan).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSeatingPlan(@PathParam("id") int id) {
        try {
            planningTableDao.deleteSeatingPlan(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
}
