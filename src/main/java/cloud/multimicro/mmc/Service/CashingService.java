/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.CashingDao;
import cloud.multimicro.mmc.Entity.RemittanceInBank;
import cloud.multimicro.mmc.Entity.RemittanceInBankDetail;
import cloud.multimicro.mmc.Entity.TMmcModeEncaissement;
import cloud.multimicro.mmc.Entity.TPmsEncaissement;
import cloud.multimicro.mmc.Entity.TPosEncaissement;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

/**
 *
 * @author Naly
 */
@Stateless
@Path("cashing")
@Produces(MediaType.APPLICATION_JSON)
public class CashingService {
    private static final Logger LOGGER = Logger.getLogger(CashingService.class);
    @Inject
    CashingDao cashingDao;

    // CASHING MODE
    @GET
    @Path("/mode")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TMmcModeEncaissement> collectionMode = cashingDao.getAll();
        if (collectionMode.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(collectionMode, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/mode/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModeEncaissementById(@PathParam("id") int id) {
        TMmcModeEncaissement collectionMode = cashingDao.getModeEncaissementById(id);
        if (collectionMode == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(collectionMode, MediaType.APPLICATION_JSON).build();
    }

    @Path("/mode")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addModeEncaissement(TMmcModeEncaissement modeEncaissement) {
        try {
            cashingDao.setModeEncaissement(modeEncaissement);
            return Response.status(Response.Status.CREATED).entity(modeEncaissement).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/mode")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateModeEncaissement(TMmcModeEncaissement modeEncaissement) {
        try {
            cashingDao.updateModeEncaissement(modeEncaissement);
            return Response.status(Response.Status.OK).entity(modeEncaissement).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/mode/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteModeEncaissement(@PathParam("id") int id) {
        try {
            cashingDao.deleteModeEncaissement(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // CASHING
    @Path("/pms")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsEncaissement() {
        List<TPmsEncaissement> encaissement = cashingDao.getPmsEncaissement();
        if (encaissement.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(encaissement, MediaType.APPLICATION_JSON).build();
    }

    // CASHING
    @Path("/remittance-in-banks-detailed")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRemittanceInBanksDetailed(@Context UriInfo info) throws ParseException {
        MultivaluedMap<String, String> parameters = info.getQueryParameters();
        String begin = parameters.getFirst("dateDebut");
        String end = parameters.getFirst("dateFin");
        String cashingMode = parameters.getFirst("modeEncaissement");
        List<RemittanceInBankDetail> remittanceInBanks = new ArrayList<>();

        remittanceInBanks = cashingDao.getRemittanceInBanksDetailed(begin, end, cashingMode);

        if (remittanceInBanks.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(remittanceInBanks, MediaType.APPLICATION_JSON).build();
    }

    // CASHING
    @Path("/remittance-in-banks")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRemittanceInBanks(@Context UriInfo info) {
        MultivaluedMap<String, String> parameters = info.getQueryParameters();
        String begin = parameters.getFirst("dateDebut");
        String end = parameters.getFirst("dateFin");
        String activity = parameters.getFirst("activity");
        List<RemittanceInBank> remittanceInBanks = new ArrayList<>();

        remittanceInBanks = cashingDao.getRemittanceInBanks(begin, end, activity);

        if (remittanceInBanks.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(remittanceInBanks, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pms/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsEncaissementById(@PathParam("id") int id) {
        TPmsEncaissement collection = cashingDao.getPmsEncaissementById(id);
        if (collection == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(collection, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pms")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPmsEncaissement(TPmsEncaissement encaissement) throws ParseException {
        try {
            cashingDao.setPmsEncaissement(encaissement);
            return Response.status(Response.Status.CREATED).entity(encaissement).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/pms")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePmsEncaissement(TPmsEncaissement encaissement) {
        try {
            cashingDao.updatePmsEncaissement(encaissement);
            return Response.status(Response.Status.OK).entity(encaissement).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/pms/by-note-header/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCashingByNoteHeaderId(@PathParam("id") Integer id) {
        List<TPmsEncaissement> encaissement = cashingDao.getCashingByNoteHeaderId(id);
        if (encaissement.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(encaissement, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosEncaissement() {
        List<TPosEncaissement> encaissement = cashingDao.getPosEncaissement();
        if (encaissement.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(encaissement, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosEncaissementById(@PathParam("id") int id) {
        TPosEncaissement collection = cashingDao.getPosEncaissementById(id);
        if (collection == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(collection, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPosEncaissement(List<TPosEncaissement> encaissement) {
        try {
            for(int i = 0; i < encaissement.size(); i++){
                cashingDao.setPosEncaissement(encaissement.get(i));
            }
            return Response.status(Response.Status.CREATED).entity(encaissement).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/refund-arrhe")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response refundArrhe(JsonObject object) throws DataException, ParseException {
        try {
            cashingDao.refundArrhe(object);
            return Response.status(Response.Status.CREATED).entity(object).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
