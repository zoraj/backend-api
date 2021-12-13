/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.RevenueDao;
import cloud.multimicro.mmc.Entity.VPmsCa;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Tsiory-PC
 */
@Stateless
@Path("revenue-pms")
@Produces(MediaType.APPLICATION_JSON)
public class RevenueService {
    @Inject
    RevenueDao revenueDao;
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCaPms() {
        List<VPmsCa> caLists = revenueDao.getVPmsCa();
        if (caLists.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(caLists, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/list-ca-by-family")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListCaByFamily(@Context UriInfo info) {
        String dateReference = info.getQueryParameters().getFirst("dateReference");
        JsonArray caList = revenueDao.getAllListCaByFamily(dateReference);
        if (caList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(caList, MediaType.APPLICATION_JSON).build();
    }
    
}
