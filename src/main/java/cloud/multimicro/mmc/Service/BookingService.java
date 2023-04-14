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
import cloud.multimicro.mmc.Entity.TMmcCodePromo;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;

import java.lang.System.Logger.Level;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.NoResultException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    
    @GET
    @Path("/codepromo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCodePromos() {
        List<TMmcCodePromo> codepromos = bookingDao.getCodePromos();
        if (codepromos.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(codepromos, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/codepromo/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCodePromo(@PathParam("id") int id) {
        TMmcCodePromo codepromo = bookingDao.getCodePromoById(id);
        if (codepromo == null) {
            throw new NotFoundException();
        }
        return Response.ok(codepromo, MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Path("/codepromo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCodePromo(JsonObject request) {
        String code = request.getString("code");
        String limite_utilisation = request.getString("limite_utilisation");
        String type_remise = request.getString("type_remise");
        String valeur = request.getString("valeur");
        String date_expiration = request.getString("date_expiration");
        String descr = request.getString("descr");
        String min_max = request.getString("min_max");
        
        try {
            Integer limiteUtilisation = (limite_utilisation.equals("") ? null : Integer.parseInt(limite_utilisation));
            bookingDao.insertCodePromo(code, limiteUtilisation, type_remise, new BigDecimal(valeur),
                                        LocalDate.parse(date_expiration), descr, min_max);
            return Response.status(Response.Status.CREATED).entity(request).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/codepromo/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCodePromo(@PathParam("id") int id) {
        try {
            bookingDao.deleteCodePromo(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @PUT
    @Path("/codepromo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCodePromo(JsonObject request) {
        String pk = request.getString("pk");
        String limite_utilisation = request.getString("limite_utilisation");
        String type_remise = request.getString("type_remise");
        String valeur = request.getString("valeur");
        String date_expiration = request.getString("date_expiration");
        String descr = request.getString("descr");
        String min_max = request.getString("min_max");
        
        try {
            Integer limiteUtilisation = (limite_utilisation.equals("") ? null : Integer.parseInt(limite_utilisation));
            bookingDao.updateCodePromo(Integer.parseInt(pk), limiteUtilisation, type_remise, new BigDecimal(valeur),
                                        LocalDate.parse(date_expiration), descr, min_max);
            return Response.status(Response.Status.OK).entity(request).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @GET
    @Path("/codepromo/isvalid/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isCodePromoValid(@PathParam("code") String code) {
        boolean isValid = bookingDao.isCodePromoValid(code);
        return Response.ok(isValid, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/codepromo/bycode/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCodePromoByCode(@PathParam("code") String code) {
        try {
            TMmcCodePromo codePromo = bookingDao.getCodePromoByCode(code);
            return Response.ok(codePromo, MediaType.APPLICATION_JSON).build();
        } catch(Exception ex) {
            return Response.ok(null, MediaType.APPLICATION_JSON).build();
        }
    }
    
}