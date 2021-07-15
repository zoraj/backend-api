package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.ReportingCrmDao;
import cloud.multimicro.mmc.Entity.VComEditionSoldeCompte;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
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
@Path("crm")
@Produces(MediaType.APPLICATION_JSON)
public class ReportingCrmService {

    private static final Logger LOGGER = Logger.getLogger(ReportingCrmService.class);
    @Inject
    ReportingCrmDao reportingCrmDao;

    // COMMERCIALE
    @GET
    @Path("/report-solde-compte")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSoldeCompte(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        String sold = info.getQueryParameters().getFirst("sold");
        String consomme = info.getQueryParameters().getFirst("consomme");

        List<VComEditionSoldeCompte> soldeCompte = reportingCrmDao.getAllSoldeCompte(dateStart, dateEnd, sold,
                consomme);
        if (soldeCompte.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(soldeCompte, MediaType.APPLICATION_JSON).build();
    }

    // COMMERCIALE
    @GET
    @Path("/report-detail-compte")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDetailDesComptes(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");

        JsonObject soldeCompte = reportingCrmDao.getAllDetailDesComptes(dateStart, dateEnd);
        if (soldeCompte.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(soldeCompte, MediaType.APPLICATION_JSON).build();
    }
}
