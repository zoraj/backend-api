/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.VATDao;
import cloud.multimicro.mmc.Entity.TMmcTva;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("vat")
public class VATService {
    @Inject
    VATDao vatDao;

    // GET FAMILY
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFamilies() {
        List<TMmcTva> list = vatDao.getAll();
        if (list.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(list, MediaType.APPLICATION_JSON).build();
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(TMmcTva tva) {
        try {
            vatDao.add(tva);
            return Response.status(Response.Status.CREATED).entity(tva).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(TMmcTva tva) {
        try {
            vatDao.update(tva);
            return Response.status(Response.Status.OK).entity(tva).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
