/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Service;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import cloud.multimicro.mmc.Dao.RoomDao;
import cloud.multimicro.mmc.Entity.TPmsCategorieChambre;
import cloud.multimicro.mmc.Entity.TPmsChambre;
import cloud.multimicro.mmc.Entity.TPmsChambreHorsService;
import cloud.multimicro.mmc.Entity.TPmsModelTarif;
import cloud.multimicro.mmc.Entity.TPmsTypeChambre;
import cloud.multimicro.mmc.Entity.TPmsTypeChambrePhoto;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * RoomService
 */
@Stateless
@Path("rooms")
@Produces(MediaType.APPLICATION_JSON)
public class RoomService {

    @Inject
    RoomDao roomDao;

    // ROOM CATEGORIES
    @Path("/categories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomCategories() {
        List<TPmsCategorieChambre> roomCategories = roomDao.getRoomCategories();
        if (roomCategories.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(roomCategories, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/categories/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomCategoriesById(@PathParam("id") int id) {
        TPmsCategorieChambre roomCategories = roomDao.getRoomCategoriesById(id);
        if (roomCategories == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(roomCategories, MediaType.APPLICATION_JSON).build();
    }

    @Path("/categories")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoom(TPmsCategorieChambre categorie) {
        try {
            roomDao.updateRoomCategories(categorie);
            return Response.status(Response.Status.CREATED).entity(categorie).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/categories")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoomCategories(TPmsCategorieChambre categorie) {
        try {
            roomDao.setRoomCategories(categorie);
            return Response.status(Response.Status.CREATED).entity(categorie).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/categories/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoomCategorie(@PathParam("id") int id) {
        try {
            if (id == 1) {
                throw new ForbiddenException();
            } else {
                roomDao.deleteRoomCategories(id);
            }
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // ROOM TYPES
    @Path("/type")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomTypes() {
        List<TPmsTypeChambre> roomTypes = roomDao.getRoomTypes();
        if (roomTypes.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(roomTypes, MediaType.APPLICATION_JSON).build();
    }

    @Path("/type/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomTypesById(@PathParam("id") int id) {
        TPmsTypeChambre roomTypes = roomDao.getRoomTypesById(id);
        if (roomTypes == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(roomTypes, MediaType.APPLICATION_JSON).build();
    }

    @Path("/type")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoomTypes(TPmsTypeChambre type) {
        try {
            roomDao.setRoomTypes(type);
            return Response.status(Response.Status.OK).entity(type).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/type")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoomTypes(TPmsTypeChambre typeChambre) {
        try {
            roomDao.updateRoomTypes(typeChambre);
            return Response.status(Response.Status.OK).entity(typeChambre).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/type/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoomTypes(@PathParam("id") int id) {
        try {
            roomDao.deleteRoomTypes(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // ********************************************************
    @Path("/type/image/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomTypesImageById(@PathParam("id") int id) {
        TPmsTypeChambrePhoto roomTypesImage = roomDao.getRoomTypesImageById(id);
        if (roomTypesImage == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(roomTypesImage, MediaType.APPLICATION_JSON).build();
    }

    @Path("/type/image")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoomTypesImage(JsonObject typeImage) {
        try {
            roomDao.setRoomTypesImage(typeImage);
            return Response.status(Response.Status.OK).entity(typeImage).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    // ********************************************************
    @Path("/type/image/by-room-type/{pmsTypeChambreId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomTypesImageByRoomType(@PathParam("pmsTypeChambreId") int pmsTypeChambreId) {
        List<TPmsTypeChambrePhoto> roomTypesImage = roomDao.getRoomTypesImageByRoomType(pmsTypeChambreId);
        if (roomTypesImage.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(roomTypesImage, MediaType.APPLICATION_JSON).build();
    }

    @Path("/type/image/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTypeImage(@PathParam("id") int id) {
        try {
            roomDao.deleteRoomTypesImage(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // ROOMS
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRooms() {
        List<TPmsChambre> rooms = roomDao.getRooms();
        if (rooms.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(rooms, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomsById(@PathParam("id") int id) {
        TPmsChambre rooms = roomDao.getRoomsById(id);
        if (rooms == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(rooms, MediaType.APPLICATION_JSON).build();
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRooms(TPmsChambre room) {
        try {
            roomDao.updateRoom(room);
            return Response.status(Response.Status.OK).entity(room).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRooms(TPmsChambre rooms) {
        try {
            roomDao.setRooms(rooms);
            return Response.status(Response.Status.CREATED).entity(rooms).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("id") int id) {
        try {
            roomDao.deleteRoom(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/pms/applicable-rate-by-room-type/{room-id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModelTarifByTypeChambre(@PathParam("room-id") int id) {
        List<TPmsModelTarif> tarifModel = roomDao.getModelTarifByTypeChambre(id);
        if (tarifModel.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(tarifModel, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pms/applicable-rate-by-room-type")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoomTypeApplicableRate(JsonObject jsonObject) {
        try {
            roomDao.addRoomTypeApplicableRate(jsonObject);
            return Response.status(Response.Status.CREATED).entity(jsonObject).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    // CRUD OUT OF ORDER
    @GET
    @Path("/out-of-order/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTPmsChambreHorsService() {
        List<TPmsChambreHorsService> saison = roomDao.getAllTPmsChambreHorsService();
        if (saison.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(saison, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/out-of-order/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTPmsChambreHorsServiceById(@PathParam("id") int id) {
        TPmsChambreHorsService saison = roomDao.getTPmsChambreHorsServiceById(id);
        if (saison == null) {
            throw new NotFoundException();
        }
        return Response.ok(saison, MediaType.APPLICATION_JSON).build();
    }

    @Path("/out-of-order/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTPmsChambreHorsService(TPmsChambreHorsService pmsChambreHorsService) throws ParseException {
        try {
            roomDao.addTPmsChambreHorsService(pmsChambreHorsService);
            return Response.status(Response.Status.CREATED).entity(pmsChambreHorsService).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/out-of-order/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSeason(TPmsChambreHorsService pmsChambreHorsService) {
        try {
            roomDao.updateTPmsChambreHorsService(pmsChambreHorsService);
            return Response.status(Response.Status.OK).entity(pmsChambreHorsService).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/out-of-order/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSeason(@PathParam("id") int id) {
        try {
            roomDao.deleteTPmsChambreHorsService(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    //Etat remplissage annuel
    @GET
    @Path("/availabilities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomAvailability(@Context UriInfo info) {
        String dateStart = info.getQueryParameters().getFirst("dateStart");
        String dateEnd = info.getQueryParameters().getFirst("dateEnd");
        JsonObject remplissageAnnuel = roomDao.getRoomAvailability(dateStart, dateEnd);
        if (remplissageAnnuel == null) {
            throw new NotFoundException();
        }
        return Response.ok(remplissageAnnuel, MediaType.APPLICATION_JSON).build();
    }

}
