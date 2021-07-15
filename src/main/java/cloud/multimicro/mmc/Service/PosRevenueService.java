package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.PosRevenueDao;
import cloud.multimicro.mmc.Entity.VPosCa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("revenue")
@Produces(MediaType.APPLICATION_JSON)
public class PosRevenueService {
    @Inject
    PosRevenueDao posRevenueDao;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVPosCaByDate(@Context UriInfo info) {
        MultivaluedMap<String, String> parameters = info.getQueryParameters();
        String dateStartString = parameters.getFirst("dateDebut");
        String dateEndString = parameters.getFirst("dateFin");

        LocalDate dateStart = LocalDate.parse(dateStartString);
        LocalDate dateEnd = LocalDate.parse(dateEndString);

        List<VPosCa> lists = posRevenueDao.getVPosCaByDate(dateStart, dateEnd);
        if (lists.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(lists, MediaType.APPLICATION_JSON).build();
    }

    @Path("/by-month")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVPosCaByMonth(@Context UriInfo info) {
        MultivaluedMap<String, String> parameters = info.getQueryParameters();
        String dateStartString = parameters.getFirst("dateDebut");
        String dateEndString = parameters.getFirst("dateFin");

        LocalDate dateStart = LocalDate.parse(dateStartString);
        LocalDate dateEnd = LocalDate.parse(dateEndString);

        List<VPosCa> lists = posRevenueDao.getVPosCaByMonth(dateStart, dateEnd);
        if (lists.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(lists, MediaType.APPLICATION_JSON).build();
    }

    @Path("/by-year")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVPosCaByYear(@Context UriInfo info) {
        MultivaluedMap<String, String> parameters = info.getQueryParameters();
        String dateStartString = parameters.getFirst("dateDebut");
        String dateEndString = parameters.getFirst("dateFin");

        LocalDate dateStart = LocalDate.parse(dateStartString);
        LocalDate dateEnd = LocalDate.parse(dateEndString);

        List<VPosCa> lists = posRevenueDao.getVPosCaByYear(dateStart, dateEnd);
        if (lists.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(lists, MediaType.APPLICATION_JSON).build();
    }

    @Path("/by-device")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVPosCaByPoste() {
        LocalDate dateLogicielle = posRevenueDao.getDateLogicielle();
        List<VPosCa> lists = posRevenueDao.getVPosCaByPoste(dateLogicielle);
        if (lists.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(lists, MediaType.APPLICATION_JSON).build();
    }

}
