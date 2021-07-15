/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.CustomerGridDao;
import cloud.multimicro.mmc.Entity.TMmcSociete;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("customer-grid")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerGridService {

    private static final Logger LOGGER = Logger.getLogger(ClientService.class);
    @Inject
    CustomerGridDao customerGridDao;

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postCustomerGrid(JsonObject object) {
        try {
            customerGridDao.postCustomerGrid(object);
            return Response.status(Response.Status.CREATED).entity(object).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
