/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.ReservationDao;
import cloud.multimicro.mmc.Entity.TPmsReservation;
import cloud.multimicro.mmc.Entity.TPmsReservationVentillation;
import cloud.multimicro.mmc.Entity.TPmsReservationTarif;
import cloud.multimicro.mmc.Entity.TPmsReservationTarifPrestation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import cloud.multimicro.mmc.Util.Payload;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

import org.jboss.logging.Logger;
import javax.json.JsonObject;

/**
 * ReservationService
 */
@Stateless
@Path("reservation")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationService {
    private static final Logger LOGGER = Logger.getLogger(ReservationService.class);
    @Inject
    ReservationDao reservationDao;
    
    //reservation
    @GET
    @Path("/get-all")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context UriInfo info) {
        MultivaluedMap<String, String> parameters = info.getQueryParameters();

        String arrival = parameters.getFirst("dateArrivee");
        String departure = parameters.getFirst("dateDepart");
        String name = parameters.getFirst("name");
        String statut = parameters.getFirst("statut");
        String client = parameters.getFirst("client");
        String origin = parameters.getFirst("origin");
        String numbooking = parameters.getFirst("numeroReservation");
        String reservationType = parameters.getFirst("reservationType");

        List<TPmsReservation> reservation = reservationDao.getAll(arrival, departure, name, statut, client, origin, numbooking, reservationType);
        if (reservation.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResaById(@PathParam("id") int id) {
        TPmsReservation reservation = reservationDao.getResaById(id);        
        if (reservation == null) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(JsonObject pmsReservation) throws ParseException, DataException {
        try {
            // Check for mandatories fields
            var mandatoryFields = Stream.of("dateArrivee", "dateDepart", "nomReservation", "nbChambre", "nbPax", "reservationType", "origine")
                .collect(Collectors.toCollection(HashSet::new));
            String missingFields = Payload.isOk(pmsReservation, mandatoryFields);
            if (!Objects.isNull(missingFields))
                throw new CustomConstraintViolationException("Mandatory fields: " + missingFields);

            // Persists data
            TPmsReservation reservation = reservationDao.add(pmsReservation);
            return Response.status(Response.Status.CREATED).entity(reservation).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(TPmsReservation pmsReservation) {
        try {
            reservationDao.update(pmsReservation);
            return Response.status(Response.Status.OK).entity(pmsReservation).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        try {
            reservationDao.delete(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }

    //reservation facture
    @GET
    @Path("/ventillation")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReservationVentillation() {
        List<TPmsReservationVentillation> reservation = reservationDao.getAllReservationVentillation();
        if (reservation.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();       
    }

    @GET
    @Path("/ventillation/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservationVentillationByReservation(@PathParam("id") Integer id) {
        List<TPmsReservationVentillation> reservation = reservationDao.getReservationVentillationByReservation(id);        
        if (reservation.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();   
    }
    
    @POST
    @Path("/ventillation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReservationVentillation(JsonObject object) {
        try {
            reservationDao.addReservationVentillation(object);
            return Response.status(Response.Status.CREATED).entity(object).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    
    @Path("/ventillation")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReservationFacture(TPmsReservationVentillation pmsReservation) {
        try {
            reservationDao.updateReservationVentillation(pmsReservation);
            return Response.status(Response.Status.OK).entity(pmsReservation).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/ventillation/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReservationVentillation(@PathParam("id") int id) {
        try {
            reservationDao.deleteReservationVentillation(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }

    //reservation rate
    @GET
    @Path("/rate")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReservationRate() {
        List<TPmsReservationTarif> reservation = reservationDao.getAllReservationRate();
        if (reservation.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/rate/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservationRateById(@PathParam("id") int id) {
        TPmsReservationTarif reservation = reservationDao.getReservationRateById(id);        
        if (reservation == null) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/rate")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReservationRate(TPmsReservationTarif pmsReservation) {
        try {
            reservationDao.updateReservationRate(pmsReservation);
            return Response.status(Response.Status.OK).entity(pmsReservation).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/rate/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReservationRate(@PathParam("id") int id) {
        try {
            reservationDao.deleteReservationRate(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }
    
    //Reservation tarif prestation
    @GET
    @Path("/rate/product")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservationTarifPrestation() {
        List<TPmsReservationTarifPrestation> reservationTarifPresta = reservationDao.getReservationTarifPrestation();
        if (reservationTarifPresta.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reservationTarifPresta, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/rate/product/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservationTarifPrestationById(@PathParam("id") int id) {
        TPmsReservationTarifPrestation reservationTarifPresta = reservationDao.getReservationTarifPrestationById(id);        
        if (reservationTarifPresta == null) {
            throw new NotFoundException();
        }
        return Response.ok(reservationTarifPresta, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/rate/product")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReservationTarifPrestation(TPmsReservationTarifPrestation reservationTarifPresta) {
        try {
            reservationDao.setReservationTarifPrestation(reservationTarifPresta);
            return Response.status(Response.Status.CREATED).entity(reservationTarifPresta).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/rate/product")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReservationTarifPrestation(TPmsReservationTarifPrestation reservationTarifPresta) {
        try {
            reservationDao.updateReservationTarifPrestation(reservationTarifPresta);
            return Response.status(Response.Status.OK).entity(reservationTarifPresta).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
