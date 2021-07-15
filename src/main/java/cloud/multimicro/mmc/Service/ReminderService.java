/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.ReminderDao;
import cloud.multimicro.mmc.Entity.TMmcRelance;
import cloud.multimicro.mmc.Entity.TMmcRelanceClient;
//import cloud.multimicro.mmc.Entity.TPmsNoteEntete;
import cloud.multimicro.mmc.Entity.VPmsRelanceClient;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("reminder")
@Produces(MediaType.APPLICATION_JSON)
public class ReminderService {

    private static final Logger LOGGER = Logger.getLogger(BookingService.class);
    @Inject
    ReminderDao reminderDao;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TMmcRelance> reminder = reminderDao.getAll();
        if (reminder.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reminder, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Integer id) {
        TMmcRelance reminder = reminderDao.getById(id);
        if (reminder == null) {
            throw new NotFoundException();
        }
        return Response.ok(reminder, MediaType.APPLICATION_JSON).build();
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(TMmcRelance reminder) {
        try {
            reminderDao.add(reminder);
            return Response.status(Response.Status.CREATED).entity(reminder).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(TMmcRelance reminder) {
        try {
            reminderDao.update(reminder);
            return Response.status(Response.Status.OK).entity(reminder).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        try {
            reminderDao.delete(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/client")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReminderClient(TMmcRelanceClient reminderClient) {
        try {
            reminderDao.addReminderClient(reminderClient);
            return Response.status(Response.Status.CREATED).entity(reminderClient).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/sending")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response send(@Context UriInfo info) {
        try {
      //      TPmsNoteEntete pmsNoteEntete = reminderDao.sendReminder(info.getQueryParameters());
       //     JsonObject value = Json.createObjectBuilder().add("content", pmsNoteEntete.getContentReminder()).build();
            return Response.ok(info, MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            LOGGER.error("Something went wrong." + ex.getMessage());
            //ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.toString()).build();
        }
    }

    @GET
    @Path("/client")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllVPmsRelanceClient() {
        List<VPmsRelanceClient> reminder = reminderDao.getAllVPmsRelanceClient();
        if (reminder.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reminder, MediaType.APPLICATION_JSON).build();
    }
}
