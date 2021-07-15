
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.PosCheckoutDao;
import cloud.multimicro.mmc.Entity.VPosEncaissement;

import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("income")
@Produces(MediaType.APPLICATION_JSON)
public class PosCheckoutService {
    private static final Logger LOGGER = Logger.getLogger(PosCheckoutService.class);
    @Inject
    PosCheckoutDao posCheckoutDao;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCashing() {
        List<VPosEncaissement> cashing = posCheckoutDao.getVPosEncaissement();
        if (cashing.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(cashing, MediaType.APPLICATION_JSON).build();
    }

    @Path("/by-device")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVPosEncaissementByDevice() {
        LocalDate dateLogicielle = posCheckoutDao.getDateLogicielle();
        List<VPosEncaissement> lists = posCheckoutDao.getVPosEncaissementByDevice(dateLogicielle);
        if (lists.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(lists, MediaType.APPLICATION_JSON).build();
    }

}
