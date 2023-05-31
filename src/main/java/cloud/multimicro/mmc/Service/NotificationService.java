package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.NotificationDao;
import cloud.multimicro.mmc.Entity.TMmcNotification;
import cloud.multimicro.mmc.Exception.DataException;
import java.text.ParseException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("notification")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationService {
    
    @Inject
    NotificationDao notificationDao;
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNotif(JsonObject pmsReservation) throws ParseException, DataException {
        TMmcNotification notif = notificationDao.createNotif(pmsReservation);
        return Response.status(Response.Status.CREATED).entity(notif).build();
    }
    
    @Path("/nonlus")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotifNonLus() throws ParseException, DataException {
        List<Long> ret = notificationDao.getAllIdNotifResaNonlu();
        if (ret.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(ret, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/modif")
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateNotif(TMmcNotification notif) {
        TMmcNotification ret = notificationDao.updateNotif(notif);
        return Response.status(Response.Status.OK).entity(ret).build();
    }
}