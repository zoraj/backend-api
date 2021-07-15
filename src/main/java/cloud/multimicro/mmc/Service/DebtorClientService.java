/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.DebtorClientDao;
import cloud.multimicro.mmc.Entity.VPmsClientDebiteur;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
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
@Path("debtor-client")
@Produces(MediaType.APPLICATION_JSON)
public class DebtorClientService {
   
    private static final Logger LOGGER = Logger.getLogger(ClientService.class);
    @Inject
    DebtorClientDao debtorClientDao;

    @Path("/pms")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<VPmsClientDebiteur> debtorClientList = debtorClientDao.getAll();
        if (debtorClientList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(debtorClientList, MediaType.APPLICATION_JSON).build();
    }
}

