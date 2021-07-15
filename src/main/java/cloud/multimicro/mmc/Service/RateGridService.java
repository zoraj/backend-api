/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.RateGridDao;
import cloud.multimicro.mmc.Entity.TPmsTarifGrille;
import cloud.multimicro.mmc.Entity.VPmsTarifGrille;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
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
 * @author Tsiory
 */
@Stateless
@Path("rate-grid")
@Produces(MediaType.APPLICATION_JSON)
public class RateGridService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class);
    @Inject
    RateGridDao rateDao;

    //Client
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRateGrid(@Context UriInfo info) {
        String season = info.getQueryParameters().getFirst("season");
        String subSeason = info.getQueryParameters().getFirst("sub-season");
        List<VPmsTarifGrille> result = rateDao.getRateGrid(season, subSeason);
        if (result.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response set(TPmsTarifGrille pmsTarifGrille) {
        try {
            rateDao.set(pmsTarifGrille);
            return Response.status(Response.Status.CREATED).entity(pmsTarifGrille).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        try {
            rateDao.delete(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
