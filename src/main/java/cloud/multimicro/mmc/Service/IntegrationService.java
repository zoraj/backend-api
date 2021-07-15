/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.IntegrationDao;
import cloud.multimicro.mmc.Entity.TMmcParametrageIntegrationComptable;
import cloud.multimicro.mmc.Entity.VPmsIntegrationVente;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

/**
 *
 * @author HERIZO RA
 */
@Stateless
@Path("Integration")
@Produces(MediaType.APPLICATION_JSON)
public class IntegrationService {
    private static final Logger LOGGER = Logger.getLogger(IntegrationService.class);
    @Inject
    IntegrationDao IntegrationDao;

    // ParametrageIntegrationComptable
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TMmcParametrageIntegrationComptable> ParametrageIntegration = IntegrationDao.getAll();
        if (ParametrageIntegration.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(ParametrageIntegration, MediaType.APPLICATION_JSON).build();
    }
    /*
     * @Path("/sub-families")
     * 
     * @GET
     * 
     * @Produces(MediaType.APPLICATION_JSON) public Response getSubFamily() {
     * List<TMmcParametrageIntegrationComptable> headers =
     * IntegrationDao.getSubFamily(); if (headers.isEmpty()) { throw new
     * NotFoundException(); } return Response.ok(headers,
     * MediaType.APPLICATION_JSON).build(); }
     */

    // GET ParametrageIntegrationComptable BY ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParametrageIntegrationComptableById(@PathParam("id") int id) {
        TMmcParametrageIntegrationComptable paramIntegration = IntegrationDao
                .getParametrageIntegrationComptableById(id);
        if (paramIntegration == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(paramIntegration, MediaType.APPLICATION_JSON).build();
    }

    // ADD ParametrageIntegrationComptable
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setParametrageIntegrationComptable(TMmcParametrageIntegrationComptable paramIntegration) {
        try {
            IntegrationDao.setParametrageIntegrationComptable(paramIntegration);
            return Response.status(Response.Status.CREATED).entity(paramIntegration).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateParametrageIntegrationComptable(TMmcParametrageIntegrationComptable paramIntegration) {
        try {
            IntegrationDao.updateParametrageIntegrationComptable(paramIntegration);
            return Response.status(Response.Status.OK).entity(paramIntegration).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteParametrageIntegrationComptable(@PathParam("id") int id) {
        try {
            IntegrationDao.deleteParametrageIntegrationComptable(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/sales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalesIntegrationsData(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonObject ParametrageIntegration = IntegrationDao.getSalesIntegrationsData(dateStart, dateEnd);
        if (ParametrageIntegration == null) {
            throw new NotFoundException();
        }
        return Response.ok(ParametrageIntegration, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/sales-cumulation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalesIntegrationsDataCumulation(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonObject ParametrageIntegration = IntegrationDao.getSalesIntegrationsDataCumulation(dateStart, dateEnd);
        if (ParametrageIntegration == null) {
            throw new NotFoundException();
        }
        return Response.ok(ParametrageIntegration, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/apercu-sales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApercuSalesIntegrationsData(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonObject ParametrageIntegration = IntegrationDao.getApercuSalesIntegrationsData(dateStart, dateEnd);
        if (ParametrageIntegration == null) {
            throw new NotFoundException();
        }
        return Response.ok(ParametrageIntegration, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/apercu-sales-cumulation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApercuSalesIntegrationsDataCumulation(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonObject ParametrageIntegration = IntegrationDao.getApercuSalesIntegrationsDataCumulation(dateStart, dateEnd);
        if (ParametrageIntegration == null) {
            throw new NotFoundException();
        }
        return Response.ok(ParametrageIntegration, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/apercu-sales-file")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response createApercuSalesIntegrationsData(@Context UriInfo info) throws IOException {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        File file = IntegrationDao.createApercuSalesIntegrationsData(dateStart, dateEnd);
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"") // optional
                .build();
    }

    @GET
    @Path("/apercu-sales-cumulation-file")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response createApercuSalesIntegrationsCumulationData(@Context UriInfo info) throws IOException {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        File file = IntegrationDao.createApercuSalesIntegrationsCumulationData(dateStart, dateEnd);
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"") // optional
                .build();
    }
}
