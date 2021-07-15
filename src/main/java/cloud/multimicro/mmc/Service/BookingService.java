/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.BookingDao;
import cloud.multimicro.mmc.Entity.BookingAvailability;
import cloud.multimicro.mmc.Entity.RoomByType;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;

import java.lang.System.Logger.Level;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONObject;
import org.jboss.logging.Logger;
/**
 * BookingService
 */
@Stateless
@Path("booking")
@Produces(MediaType.APPLICATION_JSON)
public class BookingService {
    private static final Logger LOGGER = Logger.getLogger(BookingService.class);
    @Inject
    BookingDao bookingDao;
    
    @GET
     @Path("/room-by-type")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoomByType() {
        List<RoomByType> roomList = bookingDao.getAllRoomByType();
        if (roomList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(roomList, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/room-unavailable-by-type") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOccupiedRoomId(@Context UriInfo info) {
        MultivaluedMap<String, String> parameters = info.getQueryParameters();
        String arrival = parameters.getFirst("dateArrivee");
        String departure = parameters.getFirst("dateDepart");
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<RoomByType> roomList = null;

        try {
            roomList = bookingDao.getOccupiedRoomByType(format.parse(arrival), format.parse(departure));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        if (roomList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(roomList, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/room-available-by-type") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableRoomsByType(JsonObject request) throws DataException {
        String arrival = request.getString("dateArrivee");
        String departure = request.getString("dateDepart");
        JsonArray rooms = request.getJsonArray("roomList");
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<RoomByType> roomList = null;

        try {
            roomList = bookingDao.getAvailableRoomsByType(format.parse(arrival), format.parse(departure), rooms);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        if (roomList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(roomList, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/checking") 
    @Produces(MediaType.APPLICATION_JSON)
    public Response checking(@Context UriInfo info) throws ParseException {
        MultivaluedMap<String, String> parameters = info.getQueryParameters();
        String number = parameters.getFirst("n");
        try {
             bookingDao.checking(number);
             return Response.status(Response.Status.BAD_REQUEST).entity(number).build();
        }
       catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}




