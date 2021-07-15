/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.DeviseDao;
import cloud.multimicro.mmc.Entity.TMmcDevise;
import cloud.multimicro.mmc.Entity.TMmcDeviseTauxChange;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.Date;
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
 * @author HERIZO
 */
@Stateless
@Path("Devise")
@Produces(MediaType.APPLICATION_JSON)
public class DeviseService {
    @Inject
    DeviseDao deviseDao;
    
     // GET Devise
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDevise() {
        List<TMmcDevise> devise = deviseDao.getAll();
        if (devise.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(devise, MediaType.APPLICATION_JSON).build(); 
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDevise(TMmcDevise Devise) {
        try {
            deviseDao.setDevise(Devise);
            return Response.status(Response.Status.CREATED).entity(Devise).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDevise(TMmcDevise Devise) {
        try {
            deviseDao.updateDevise(Devise);
            return Response.status(Response.Status.OK).entity(Devise).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDevise(@PathParam("id") int id) {
        try {
            deviseDao.deleteDevise(id);
            return Response.status(Response.Status.OK).entity(id).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    
     // GET Devise
    @Path("/exchange-rate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExchangeRate() {
        List<TMmcDeviseTauxChange> exchangeRate = deviseDao.getExchangeRate();
        if (exchangeRate.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(exchangeRate, MediaType.APPLICATION_JSON).build(); 
    }
    
    @Path("/exchange-rate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setExchangeRate(TMmcDeviseTauxChange exchangeRate) {
        try {
            deviseDao.setExchangeRate(exchangeRate); 
            return Response.status(Response.Status.CREATED).entity(exchangeRate).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/exchange-rate")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateExchangeRate(TMmcDeviseTauxChange exchangeRate) {
        try {
            deviseDao.updateExchangeRate(exchangeRate);
            return Response.status(Response.Status.OK).entity(exchangeRate).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/exchange-rate/{exchangeRateDate}/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExchangeRateByReferenceAndByDate(@PathParam("exchangeRateDate") String exchangeRateDate, @PathParam("id") int id) {
        List<TMmcDeviseTauxChange> exchangeRate = deviseDao.getExchangeRateByReferenceAndByDate(id,exchangeRateDate); 
        if (exchangeRate.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(exchangeRate, MediaType.APPLICATION_JSON).build(); 
    }
}


