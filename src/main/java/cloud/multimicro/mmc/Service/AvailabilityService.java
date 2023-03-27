/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.AvailabilityDao;
import cloud.multimicro.mmc.Entity.TotalRoomByType;
import cloud.multimicro.mmc.Entity.TotalRoomCountAvailable;
import cloud.multimicro.mmc.Entity.TotalRoomCountUnavailable;
import cloud.multimicro.mmc.Entity.TotalRoomOutOfOrderByType;
import cloud.multimicro.mmc.Entity.VPmsChambreDisponibiliteEtat;
import cloud.multimicro.mmc.Exception.DataException;
import java.time.LocalDate;
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
    @Path("/total-room")  
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
        try {
            LocalDate dateStart = LocalDate.parse(info.getQueryParameters().getFirst("dateStart"));
            LocalDate dateEnd = LocalDate.parse(info.getQueryParameters().getFirst("dateEnd"));
            if (dateStart.isAfter(dateEnd) ){
                throw new DataException("Start date must be before end date");   
            }
            List<TotalRoomOutOfOrderByType> outOrder = availabilityDao.getRoomCountByTypeOutOfOrder(dateStart, dateEnd);
            if (outOrder.isEmpty()) {
                throw new NotFoundException();
            }
            return Response.ok(outOrder, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("/room-unavailable")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomCountUnavailable(@Context UriInfo info) {
        try {
            LocalDate dateStart = LocalDate.parse(info.getQueryParameters().getFirst("dateStart"));
            LocalDate dateEnd = LocalDate.parse(info.getQueryParameters().getFirst("dateEnd"));
            if (dateStart.isAfter(dateEnd) ){
                throw new DataException("Start date must be before end date");   
            }
            List<TotalRoomCountUnavailable> outOrder = availabilityDao.getRoomCountUnavailable(dateStart, dateEnd);
            if (outOrder.isEmpty()) {
                throw new NotFoundException();
            }
            return Response.ok(outOrder, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("/total-rooms-available")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomCountAvailable(@Context UriInfo info) {
        try {
            LocalDate dateStart = LocalDate.parse(info.getQueryParameters().getFirst("dateStart"));
            LocalDate dateEnd = LocalDate.parse(info.getQueryParameters().getFirst("dateEnd"));
            if (dateStart.isAfter(dateEnd) ){
                throw new DataException("Start date must be before end date");   
            }
            List<TotalRoomCountAvailable> outOrder = availabilityDao.getRoomCountAvailables(dateStart, dateEnd);
            if (outOrder.isEmpty()) {
                throw new NotFoundException();
            }
            return Response.ok(outOrder, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("/availability-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomsAvailabilityStatus() {
        List<VPmsChambreDisponibiliteEtat> availableStatus = availabilityDao.getRoomsAvailabilityStatus();
        if (availableStatus.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(availableStatus, MediaType.APPLICATION_JSON).build();
    }
    
}
