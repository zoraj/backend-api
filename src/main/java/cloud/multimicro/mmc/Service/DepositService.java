/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.DepositDao;
import cloud.multimicro.mmc.Entity.TMmcModeEncaissement;
import cloud.multimicro.mmc.Entity.TPmsArrhe;
import cloud.multimicro.mmc.Entity.VPmsArrhesClient;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("deposit")
@Produces(MediaType.APPLICATION_JSON)
public class DepositService {

    @Inject
    DepositDao depositDao;

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDeposit(TPmsArrhe pmsArrhe) {
        try {
            depositDao.setDeposit(pmsArrhe);
            return Response.ok(pmsArrhe, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response putDeposit(JsonObject request) {
        try {
           TPmsArrhe newDeposit = depositDao.putDeposit(request);
            return Response.ok(newDeposit, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    // CASHING MODE
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<VPmsArrhesClient> deposits = depositDao.getAll();
        if (deposits.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(deposits, MediaType.APPLICATION_JSON).build();       
    }
}
