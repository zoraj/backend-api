/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.RateGridDetailDetailDao;
import cloud.multimicro.mmc.Entity.TPmsTarifGrilleDetailDetail;
import cloud.multimicro.mmc.Entity.VPmsTarifGrilleDetailDetail;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("rate-grid-detail-detail")
@Produces(MediaType.APPLICATION_JSON)
public class RateGridDetailDetailService {

    private static final Logger LOGGER = Logger.getLogger(BookingService.class);
    @Inject
    RateGridDetailDetailDao rateGridDetailDetailDao;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TPmsTarifGrilleDetailDetail> value = rateGridDetailDetailDao.getAll();
        if (value.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(value, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) {
        TPmsTarifGrilleDetailDetail value = rateGridDetailDetailDao.getById(id);
        if (value == null) {
            throw new NotFoundException();
        }
        return Response.ok(value, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/by-rate-detail/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("id") Integer id) {
        List<TPmsTarifGrilleDetailDetail> value = rateGridDetailDetailDao.getByRateDetail(id);
        if (value.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(value, MediaType.APPLICATION_JSON).build();
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response set(JsonObject value) {
        try {
            rateGridDetailDetailDao.set(value);
            return Response.status(Response.Status.CREATED).entity(value).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(JsonObject value) {
        try {
            rateGridDetailDetailDao.put(value);
            return Response.status(Response.Status.OK).entity(value).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        try {
            rateGridDetailDetailDao.delete(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/{dateStart}/{dateEnd}/{modelTarifId}/{base}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRateGridDetailDetail(@PathParam("dateStart") String dateStart, @PathParam("dateEnd") String dateEnd, @PathParam("modelTarifId") int modelTarifId, @PathParam("base") int base) {
        try {
            rateGridDetailDetailDao.deleteRateGridDetailDetail(dateStart, dateEnd, modelTarifId, base);
            return Response.ok(modelTarifId, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/{dateTarif}/{mmcClientId}/{tarifGrilleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTarifGrilleDetailDetail(@PathParam("dateTarif") String dateTarif, @PathParam("mmcClientId") int mmcClientId, @PathParam("tarifGrilleId") int tarifGrilleId) {
        List<VPmsTarifGrilleDetailDetail> value = rateGridDetailDetailDao.getAllTarifGrilleDetailDetail(dateTarif, mmcClientId, tarifGrilleId);
        if (value.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(value, MediaType.APPLICATION_JSON).build();
    }
}
