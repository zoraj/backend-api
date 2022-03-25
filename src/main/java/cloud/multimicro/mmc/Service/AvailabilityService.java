/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.AvailabilityDao;
import cloud.multimicro.mmc.Entity.RoomByType;
import cloud.multimicro.mmc.Entity.TPmsChambreHorsService;
import cloud.multimicro.mmc.Entity.TPmsReservationVentilation;
import cloud.multimicro.mmc.Entity.TotalRoomByType;
import cloud.multimicro.mmc.Entity.TotalRoomOutOfOrderByType;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
 * @author Tsiory-PC
 */
@Stateless
@Path("availability")
@Produces(MediaType.APPLICATION_JSON)
public class AvailabilityService {
    
    private static final Logger LOGGER = Logger.getLogger(BookingService.class);
    @Inject
    AvailabilityDao availabilityDao;
    
    @GET
    @Path("/")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomCountByType() {
        List<TotalRoomByType> roomList = availabilityDao.getRoomCountByType();
        if (roomList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(roomList, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/out-of-order")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomCountByTypeOutOfOrder(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        List<TotalRoomOutOfOrderByType> outOrder = availabilityDao.getRoomCountByTypeOutOfOrder(dateStart, dateEnd);
        if (outOrder.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(outOrder, MediaType.APPLICATION_JSON).build();
    }
    
}
