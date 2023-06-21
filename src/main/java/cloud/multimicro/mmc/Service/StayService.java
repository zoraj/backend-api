package cloud.multimicro.mmc.Service;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import cloud.multimicro.mmc.Dao.SettingDao;
import cloud.multimicro.mmc.Dao.StayDao;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;

import javax.ws.rs.PUT;

import org.jboss.logging.Logger;
import cloud.multimicro.mmc.Entity.TPmsSejour;
import cloud.multimicro.mmc.Entity.TPmsSejourTarif;
import cloud.multimicro.mmc.Entity.TPmsSejourTarifDetail;
import java.math.BigDecimal;

/**
 * SettingService
 */
@Stateless
@Path("stay")
@Produces(MediaType.APPLICATION_JSON)
public class StayService {
    private static final Logger LOGGER = Logger.getLogger(StayService.class);
    @Inject
    StayDao stayDao;
    
    //stay
    @GET
    @Path("/")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context UriInfo info) {
        MultivaluedMap<String, String> parameters = info.getQueryParameters();
        String name = parameters.getFirst("name");

        List<TPmsSejour> stay = stayDao.getAll(name);
        if (stay.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(stay, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStayById(@PathParam("id") int id) {
        TPmsSejour stay = stayDao.getStayById(id);        
        if (stay == null) {
            throw new NotFoundException();
        }
        return Response.ok(stay, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(TPmsSejour sejour) {
        try {
            stayDao.add(sejour);
            return Response.status(Response.Status.CREATED).entity(sejour).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    //stay rate
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(TPmsSejour pmsSejour) {
        try {
            stayDao.update(pmsSejour);
            return Response.status(Response.Status.OK).entity(pmsSejour).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        try {
            stayDao.delete(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }

    //stay rate
    @GET
    @Path("/rate")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReservationRate() {
        List<TPmsSejourTarif> reservation = stayDao.getAllStayRate();
        if (reservation.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/rate/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservationRateById(@PathParam("id") int id) {
        TPmsSejourTarif reservation = stayDao.getStayRateById(id);        
        if (reservation == null) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/rate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReservationRate(JsonObject pmsReservation) {
        try {
            stayDao.addStayRate(pmsReservation);
            return Response.status(Response.Status.CREATED).entity(pmsReservation).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    
    @Path("/rate")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStayRate(TPmsSejourTarif pmsSejour) {
        try {
            stayDao.updateStayRate(pmsSejour);
            return Response.status(Response.Status.OK).entity(pmsSejour).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/rate/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStayRate(@PathParam("id") int id) {
        try {
            stayDao.deleteStayRate(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }

     //stay rate
     @GET
     @Path("/rate/detailed")  
     @Produces(MediaType.APPLICATION_JSON)
     public Response getAllStayRateDetailed() {
         List<TPmsSejourTarifDetail> reservation = stayDao.getAllStayRateDetailed();
         if (reservation.isEmpty()) {
             throw new NotFoundException();
         }
         return Response.ok(reservation, MediaType.APPLICATION_JSON).build();       
     }
     
     @GET
     @Path("/rate/detailed/{id}")
     @Produces(MediaType.APPLICATION_JSON)
     public Response getStayRateDetailedById(@PathParam("id") int id) {
        TPmsSejourTarifDetail reservation = stayDao.getStayRateDetailedById(id);        
         if (reservation == null) {
             throw new NotFoundException();
         }
         return Response.ok(reservation, MediaType.APPLICATION_JSON).build();   
     }
     
     @Path("/rate/detailed")
     @POST
     @Produces(MediaType.APPLICATION_JSON)
     public Response addStayRateDetailed(JsonObject pmsReservation) {
         try {
             stayDao.addStayRateDetailed(pmsReservation);
             return Response.status(Response.Status.CREATED).entity(pmsReservation).build();
         }
         catch (CustomConstraintViolationException ex) {
             return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
         }
     }
     
     
     @Path("/rate/detailed")
     @PUT
     @Produces(MediaType.APPLICATION_JSON)
     public Response updateStayRateDetailed(TPmsSejourTarifDetail pmsSejour) {
         try {
             stayDao.updateStayRateDetailed(pmsSejour);
             return Response.status(Response.Status.OK).entity(pmsSejour).build();
         } catch (CustomConstraintViolationException e) {
             return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
         }
     }
     
     @Path("/rate/detailed/{id}")
     @DELETE
     @Produces(MediaType.APPLICATION_JSON)
     public Response deleteStayRateDetailed(@PathParam("id") int id) {
         try {
             stayDao.deleteStayRateDetailed(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
         } catch (Exception e) {
             return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
         }   
     }
     
     @GET
     @Path("/en_note/byTypeChambre/{id}/{dateLogiciel}")
     @Produces(MediaType.APPLICATION_JSON)
     public Response getEnNotesByTypechambre(@PathParam("id") int id, @PathParam("dateLogiciel") String dateLogiciel) {
        List<Long> ret = stayDao.getEnNotesByTypechambre(id, dateLogiciel);
        if (ret == null) {
            throw new NotFoundException();
        }
        return Response.ok(ret.size(), MediaType.APPLICATION_JSON).build();
     }
     
     @GET
     @Path("/soldee/byTypeChambre/{id}/{dateLogiciel}")
     @Produces(MediaType.APPLICATION_JSON)
     public Response getSoldeesByTypechambre(@PathParam("id") int id, @PathParam("dateLogiciel") String dateLogiciel) {
        List<Long> ret = stayDao.getSoldeesByTypechambre(id, dateLogiciel);
        if (ret == null) {
            throw new NotFoundException();
        }
        return Response.ok(ret.size(), MediaType.APPLICATION_JSON).build();
     }
     
     @GET
     @Path("/en_attente/byTypeChambre/{id}/{dateLogiciel}")
     @Produces(MediaType.APPLICATION_JSON)
     public Response getEnAttentesByTypechambre(@PathParam("id") int id, @PathParam("dateLogiciel") String dateLogiciel) {
        List<Long> ret = stayDao.getEnAttentesByTypechambre(id, dateLogiciel);
        if (ret == null) {
            throw new NotFoundException();
        }
        return Response.ok(ret.size(), MediaType.APPLICATION_JSON).build();
     }
     
     @GET
     @Path("/departs/byTypeChambre/{id}/{dateLogiciel}")
     @Produces(MediaType.APPLICATION_JSON)
     public Response getDepartsByTypechambre(@PathParam("id") int id, @PathParam("dateLogiciel") String dateLogiciel) {
        List<Long> ret = stayDao.getDepartsByTypechambre(id, dateLogiciel);
        if (ret == null) {
            throw new NotFoundException();
        }
        return Response.ok(ret.size(), MediaType.APPLICATION_JSON).build();
     }
     
}
