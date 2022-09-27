/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.ReportingGouvernanteDao;
import cloud.multimicro.mmc.Entity.THkElementReporting;
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
 * @author HERIZO-PC
 */
@Stateless
@Path("reporting-gouvernante")
@Produces(MediaType.APPLICATION_JSON)
public class ReportingGouvernanteService {
    
    //private static final Logger LOGGER = Logger.getLogger(ProductService.class);
    @Inject
    ReportingGouvernanteDao ReportingGouvernanteDao;
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReportingGouvernante() {
        List<THkElementReporting> reporting = ReportingGouvernanteDao.getAllReportingGouvernante();
        if (reporting.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reporting, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReportingGouvernanteId(@PathParam("id") int id) {
        THkElementReporting reporting = ReportingGouvernanteDao.getReportingGouvernanteId(id);
        if (reporting == null) {
            throw new NotFoundException();
        }
        return Response.ok(reporting, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setReportingGouvernante(THkElementReporting reporting) {
        try {
            ReportingGouvernanteDao.setReportingGouvernante(reporting);
            return Response.status(Response.Status.CREATED).entity(reporting).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStockOther(THkElementReporting reporting) {
        try {
            ReportingGouvernanteDao.updateReportingGouvernante(reporting);
            return Response.status(Response.Status.OK).entity(reporting).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStockOther(@PathParam("id") Integer id) {
        try {
            ReportingGouvernanteDao.deleteReportingGouvernante(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }
}
