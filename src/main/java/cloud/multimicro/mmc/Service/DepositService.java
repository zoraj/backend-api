/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.DepositDao;
import cloud.multimicro.mmc.Dao.SettingDao;
import cloud.multimicro.mmc.Entity.TMmcModeEncaissement;
import cloud.multimicro.mmc.Entity.TPmsArrhe;
import cloud.multimicro.mmc.Entity.VPmsArrhesClient;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.math.BigDecimal;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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
    SettingDao settingDao;
    
    @Inject
    DepositDao depositDao;

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDeposit(TPmsArrhe pmsArrhe) {
        try {
            pmsArrhe.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            depositDao.setDeposit(pmsArrhe);
            return Response.ok(pmsArrhe, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/booking")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDepositBooking(JsonObject request) {
        try {
            depositDao.setDepositBooking(request);
            return Response.ok(request, MediaType.APPLICATION_JSON).build();
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
    
    @GET
    @Path("/debit-balance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepositBalance() {
        BigDecimal depositBalance = depositDao.totalDepositBalance();
        if (depositBalance == null) {
            depositBalance = new BigDecimal(0.0);
        }
        return Response.ok(depositBalance, MediaType.APPLICATION_JSON).build();
    }
}
