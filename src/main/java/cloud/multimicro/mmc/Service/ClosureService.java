/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.ClosureDao;
import cloud.multimicro.mmc.Entity.TMmcDeviceCloture;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Entity.TPmsClotureDefinitive;
import cloud.multimicro.mmc.Entity.TPmsClotureProvisoire;
import cloud.multimicro.mmc.Entity.VPosCa;
import cloud.multimicro.mmc.Entity.VPosEncaissement;

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
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.json.JsonArray;
import javax.json.JsonObject;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Naly
 */
@Stateless
@Path("closure")
@Produces(MediaType.APPLICATION_JSON)
public class ClosureService {
    private static final Logger LOGGER = Logger.getLogger(ClosureService.class);
    @Inject
    ClosureDao closureDao;

    @Path("/pos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDateLogicielleIncrement() {
        BigInteger postNotClosed = closureDao.countPostNotClosed();

        if (postNotClosed.equals(new BigInteger("0"))) {
            TMmcParametrage dateLogicielle = closureDao.getDateLogicielleIncrement();
            return Response.ok(dateLogicielle, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @Path("/pms")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDateLogicielleIncrementPms() {
        try{
            TMmcParametrage dateLogicielle = closureDao.getDateLogicielleIncrementPms();
            return Response.ok(dateLogicielle, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TMmcDeviceCloture> object = closureDao.getAll();
        if (object.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(object, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) {
        TMmcDeviceCloture object = closureDao.getById(id);
        if (object == null) {
            throw new NotFoundException();
        }
        return Response.ok(object, MediaType.APPLICATION_JSON).build();
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(TMmcDeviceCloture object) {
        try {
            closureDao.add(object);
            return Response.status(Response.Status.CREATED).entity(object).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/list")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDeviceClotureList(List<TMmcDeviceCloture> object) {
        try {
            closureDao.addDeviceClotureList(object);
            return Response.status(Response.Status.CREATED).entity(object).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(TMmcDeviceCloture object) {
        try {
            closureDao.update(object);
        return Response.status(Response.Status.OK).entity(object).build();
        } catch (DataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }       
    }


    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        try {
            closureDao.delete(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/list-ca-by-activity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListCaByActivity() {
        JsonArray caList = closureDao.getAllListCaByActivity();
        if (caList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(caList, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/list-ca")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListCa() {
        JsonArray caList = closureDao.getAllListCa();
        if (caList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(caList, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/list-cashing-by-activity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListCashingByActivity() {
        JsonArray caList = closureDao.getAllListCashingByActivity();
        if (caList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(caList, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/list-cashing")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListCashing() {
        JsonArray caList = closureDao.getAllListCashing();
        if (caList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(caList, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/list-edition-by-activity")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEditionClosure() {
        JsonObject caList = closureDao.getAllEditionClosure();
        if (caList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(caList, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/pms/provisional")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPmsClotureProvisoire(JsonObject request) throws ParseException {
        try {
            closureDao.setPmsClotureProvisoire(request);
            return Response.ok(request, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/pms/provisional")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsClotureProvisoire(@Context UriInfo info) {
        String dateReference = info.getQueryParameters().getFirst("dateReference");
        List<TPmsClotureProvisoire> closure = closureDao.getPmsClotureProvisoire(dateReference);
        if (closure.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(closure, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/pms/definitive")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPmsClotureDefinitive(JsonObject request) throws ParseException {
        try {
            closureDao.setPmsClotureDefinitive(request);
            return Response.ok(request, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/pms/definitive")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsClotureDefinitive(@Context UriInfo info) {
        String dateReference = info.getQueryParameters().getFirst("dateReference");
        List<TPmsClotureDefinitive> closure = closureDao.getPmsClotureDefinitive(dateReference);
        if (closure.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(closure, MediaType.APPLICATION_JSON).build();
    }

}
