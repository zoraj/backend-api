/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.RateGridDetailDao;
import cloud.multimicro.mmc.Entity.TPmsTarifGrilleDetail;
import cloud.multimicro.mmc.Entity.VPmsTarifGrilleDetail;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.text.ParseException;
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
@Path("rate-grid-detail")
@Produces(MediaType.APPLICATION_JSON)
public class RateGridDetailService {

    private static final Logger LOGGER = Logger.getLogger(BookingService.class);
    @Inject
    RateGridDetailDao rateGridDetailDao;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TPmsTarifGrilleDetail> value = rateGridDetailDao.getAll();
        if (value.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(value, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByRateDetail(@PathParam("id") Integer id) {
        JsonObject value = rateGridDetailDao.getByRateDetail(id);
        if (value.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(value, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/by-rate-grid/{dateStart}/{dateEnd}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("dateStart") String dateStart, @PathParam("dateEnd") String dateEnd,
            @PathParam("id") Integer id) {
        List<VPmsTarifGrilleDetail> valueList = rateGridDetailDao.getDetailByRateGrid(dateStart, dateEnd, id);        
        if (valueList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(valueList, MediaType.APPLICATION_JSON).build();
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response set(JsonObject value) {
        try {
            List<Integer> valueIdList = rateGridDetailDao.set(value);
            return Response.status(Response.Status.CREATED).entity(valueIdList).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(JsonObject value) throws ParseException {
        try {
            rateGridDetailDao.put(value);
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
            rateGridDetailDao.delete(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
