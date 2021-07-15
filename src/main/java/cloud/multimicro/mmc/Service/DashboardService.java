package cloud.multimicro.mmc.Service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cloud.multimicro.mmc.Dao.DashboardDao;
import cloud.multimicro.mmc.Entity.VCollectiviteDashboard;
import cloud.multimicro.mmc.Entity.VCollectiviteDashboardGrapheCaMensuel;
import cloud.multimicro.mmc.Entity.VPmsDashboard;
import cloud.multimicro.mmc.Entity.VPmsDashboardCaDetail;
import cloud.multimicro.mmc.Entity.VPmsDashboardGrapheCaMensuel;
import cloud.multimicro.mmc.Entity.VPmsDashboardGrapheNombreArrivee;
import cloud.multimicro.mmc.Entity.VPosDashboard;
import cloud.multimicro.mmc.Entity.VPosDashboardCaDetail;
import cloud.multimicro.mmc.Entity.VPosDashboardGrapheCaMensuel;
import cloud.multimicro.mmc.Entity.VStatistiqueDashboard;
import javassist.NotFoundException;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardService {
    @Inject
    DashboardDao dashboardDao;

    // GET
    @GET
    @Path("/pms")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashBoard() throws NotFoundException {
        VPmsDashboard dashboard = dashboardDao.getDashBoard();
        if (dashboard == null) {
            throw new NotFoundException(null);
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(dashboard, MediaType.APPLICATION_JSON).build();
    }

    // GET FAMILY
    @Path("/pms/arrivals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashBoardGraphNombreArrivee() throws NotFoundException {
        List<VPmsDashboardGrapheNombreArrivee> dashboard = dashboardDao.getDashBoardGraphNombreArrivee();
        if (dashboard.isEmpty()) {
            throw new NotFoundException(null);
        }
        return Response.ok(dashboard, MediaType.APPLICATION_JSON).build();
    }

    // GET FAMILY
    @Path("/pms/monthly-turnover")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashBoardGraphCaMensuel() throws NotFoundException {
        List<VPmsDashboardGrapheCaMensuel> dashboard = dashboardDao.getDashBoardGraphCaMensuel();
        if (dashboard.isEmpty()) {
            throw new NotFoundException(null);
        }
        return Response.ok(dashboard, MediaType.APPLICATION_JSON).build();
    }

    // GET
    @GET
    @Path("/pms/detail-turnover")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDashBoardCaDetail() throws NotFoundException {
        VPmsDashboardCaDetail dashboard = dashboardDao.getDashBoardCaDetail();
        if (dashboard == null) {
            throw new NotFoundException(null);
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(dashboard, MediaType.APPLICATION_JSON).build();
    }

    // GET
    @GET
    @Path("/pos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosDashBoard() throws NotFoundException {
        VPosDashboard dashboard = dashboardDao.getPosDashBoard();
        if (dashboard == null) {
            throw new NotFoundException(null);
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(dashboard, MediaType.APPLICATION_JSON).build();
    }

    // GET FAMILY
    @Path("/pos/monthly-turnover")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosDashBoardGraphCaMensuel() throws NotFoundException {
        List<VPosDashboardGrapheCaMensuel> dashboard = dashboardDao.getPosDashBoardGraphCaMensuel();
        if (dashboard.isEmpty()) {
            throw new NotFoundException(null);
        }
        return Response.ok(dashboard, MediaType.APPLICATION_JSON).build();
    }

    // GET
    @GET
    @Path("/pos/detail-turnover")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosDashBoardCaDetail() throws NotFoundException {
        VPosDashboardCaDetail dashboard = dashboardDao.getPosDashBoardCaDetail();
        if (dashboard == null) {
            throw new NotFoundException(null);
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(dashboard, MediaType.APPLICATION_JSON).build();
    }

    // GET
    @GET
    @Path("/statistique")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatDashBoard() throws NotFoundException {
        VStatistiqueDashboard dashboard = dashboardDao.getStatDashBoard();
        if (dashboard == null) {
            throw new NotFoundException(null);
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(dashboard, MediaType.APPLICATION_JSON).build();
    }

    // GET
    @GET
    @Path("/collectivite")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCollectiviteDashBoard() throws NotFoundException {
        VCollectiviteDashboard dashboard = dashboardDao.getCollectiviteDashBoard();
        if (dashboard == null) {
            throw new NotFoundException(null);
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(dashboard, MediaType.APPLICATION_JSON).build();
    }

    // GET FAMILY
    @Path("/collectivite/monthly-turnover")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCollectiviteDashBoardGraphCaMensuel() throws NotFoundException {
        List<VCollectiviteDashboardGrapheCaMensuel> dashboard = dashboardDao.getCollectiviteDashBoardGraphCaMensuel();
        if (dashboard.isEmpty()) {
            throw new NotFoundException(null);
        }
        return Response.ok(dashboard, MediaType.APPLICATION_JSON).build();
    }

}
