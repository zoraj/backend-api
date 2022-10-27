/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cloud.multimicro.mmc.Dao.NoteDao;
import cloud.multimicro.mmc.Entity.MonthlyTurnover;
import cloud.multimicro.mmc.Entity.PassageHistory;
import cloud.multimicro.mmc.Entity.TPmsNoteDetail;
import cloud.multimicro.mmc.Entity.TPmsNoteEntete;
import cloud.multimicro.mmc.Entity.TPosNoteApprovision;
import cloud.multimicro.mmc.Entity.TPosNoteDetail;
import cloud.multimicro.mmc.Entity.TPosNoteDetailCommande;
import cloud.multimicro.mmc.Entity.TPosNoteEntete;
import cloud.multimicro.mmc.Entity.VPosNoteDetailVenteEmportee;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;

import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("notes")
@Produces(MediaType.APPLICATION_JSON)
public class NoteService {

    @Inject
    NoteDao noteDao;

    @Path("/pos/header")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPosNoteHeader(TPosNoteEntete headerNote) {
        try {
            //headerNote.setEtat("ENCOURS");
            noteDao.setPosNoteEntete(headerNote);
            return Response.status(Response.Status.CREATED).entity(headerNote).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    } 
    
    @Path("/pos/header")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePosNoteHeader(TPosNoteEntete note) {
        try {
            noteDao.updatePosNoteHeader(note);
            return Response.status(Response.Status.OK).entity(note).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/pos/header/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePosNoteVaeStatutAnnule(@PathParam("id") Integer id) {
        try {
            noteDao.updatePosNoteVaeStatutAnnule(id);
            return Response.status(Response.Status.CREATED).entity(id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    @Path("/pos/header/delivre/{id}")
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePosNoteVaeStatutDelivre(@PathParam("id") Integer id) {
        try {
            noteDao.updatePosNoteVaeStatutDelivre(id);
            return Response.status(Response.Status.CREATED).entity(id).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/pos/details")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPosNoteDetail(List<TPosNoteDetail> detailList) throws DataException, ParseException {
        try {
            for (int i = 0; i < detailList.size(); i++) {
                noteDao.setPosNoteDetail(detailList.get(i));
            }
            return Response.status(Response.Status.CREATED).entity(detailList).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/pos/detailscommande")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPosNoteDetailCommande(JsonObject jsonObject) {
        JsonArray listeCommande = jsonObject.getJsonArray("listeCommande");
        for (int i = 0; i < listeCommande.size(); i++) {
        try {
                noteDao.addTakeAway(listeCommande.getJsonObject(i));
               
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    return Response.status(Response.Status.CREATED).entity(listeCommande).build();
    
    }

    @Path("/pos/header")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosNoteHeader() {
        List<TPosNoteEntete> headers = noteDao.getPosNoteHeader();
        if (headers.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(headers, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/open-note")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosOpenNote() {
        List<TPosNoteEntete> headers = noteDao.getPosOpenNote();
        if (headers.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(headers, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/vae-note")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosVaeNote() {
        List<TPosNoteEntete> headers = noteDao.getPosVaeNote();
        if (headers.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(headers, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/{dateNote}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosNoteHeaderNbCouvert(@PathParam("dateNote") String dateNote) {
        Long nbCouvert = noteDao.getPosNoteHeaderNbCouvert(dateNote);
        if (nbCouvert == null) {
            throw new NotFoundException();
        }
        return Response.ok(nbCouvert, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/reservation/{dateBegin}/{dateEnd}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosReservation(@PathParam("dateBegin") String dateBegin, @PathParam("dateEnd") String dateEnd) {
        List<TPosNoteEntete> details = noteDao.getPosReservation(dateBegin, dateEnd);
        if (details.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(details, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/details")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosNoteDetail() {
        List<TPosNoteDetail> details = noteDao.getPosNoteDetail();
        if (details.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(details, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/details-by-header/{header}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosNoteDetailByHeader(@PathParam("header") int id) {
        List<TPosNoteDetail> details = noteDao.getPosNoteDetailByNoteHeaderId(id);
        if (details.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(details, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/details-vae-by-header/{header}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosNoteDetailVaeByNoteHeaderId(@PathParam("header") int id) {
        List<VPosNoteDetailVenteEmportee> details = noteDao.getPosNoteDetailVaeByNoteHeaderId(id);
        if (details.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(details, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/details/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosNoteDetailById(@PathParam("id") int id) {
        TPosNoteDetail details = noteDao.getPosNoteDetailById(id);
        if (details == null) {
            throw new NotFoundException();
        }
        return Response.ok(details, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/approvision")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosNoteApprovision() {
        List<TPosNoteApprovision> approvision = noteDao.getPosNoteApprovision();
        if (approvision.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(approvision, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/approvision/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosNoteApprovisionById(@PathParam("id") int id) {
        TPosNoteApprovision approvision = noteDao.getPosNoteApprovisionById(id);
        if (approvision == null) {
            throw new NotFoundException();
        }
        return Response.ok(approvision, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/approvision")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPosNoteApprovision(TPosNoteApprovision approvision) {
        try {
            noteDao.setPosNoteApprovision(approvision);
            return Response.status(Response.Status.CREATED).entity(approvision).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/pos/passageHistory/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPassageHistory(@PathParam("id") int id) {
        List<PassageHistory> historyList = noteDao.getPassageHistory(id);
        if (historyList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(historyList, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/day-turnover/{dateLogicielle}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDayTurnover(@PathParam("dateLogicielle") String dateLogicielle) {
        BigDecimal dayTurnover = noteDao.getDayTurnover(dateLogicielle);
        if (dayTurnover == null) {
            throw new NotFoundException();
        }
        return Response.ok(dayTurnover, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/monthly-turnover")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthlyTurnover() {
        List<MonthlyTurnover> monthlyTurnoverList = noteDao.getMonthlyTurnover();
        if (monthlyTurnoverList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(monthlyTurnoverList, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/collectivity/number-of-passes-by-day/{dateLogicielle}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNumberPassesDay(@PathParam("dateLogicielle") String dateLogicielle) {
        Long numberPasses = noteDao.getNumberPassesDay(dateLogicielle);
        if (numberPasses == null) {
            throw new NotFoundException();
        }
        return Response.ok(numberPasses, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/collectivity/cashing-day/{dateLogicielle}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCashingDayCollectivity(@PathParam("dateLogicielle") String dateLogicielle) {
        BigDecimal cashingDay = noteDao.getCashingDayCollectivity(dateLogicielle);
        if (cashingDay == null) {
            cashingDay = new BigDecimal(0.0);
        }
        return Response.ok(cashingDay, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/collectivity/turnover-day/{dateLogicielle}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDayTurnoverCollectivity(@PathParam("dateLogicielle") String dateLogicielle) {
        BigDecimal turnoverDay = noteDao.getDayTurnoverCollectivity(dateLogicielle);
        if (turnoverDay == null) {
            turnoverDay = new BigDecimal(0.0);
        }

        return Response.ok(turnoverDay, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/collectivity/annual-turnover/{dateLogicielle}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnualTurnover(@PathParam("dateLogicielle") String dateLogicielle) {
        BigDecimal annualTurnover = noteDao.getAnnualTurnover(dateLogicielle);

        if (annualTurnover == null) {
            annualTurnover = new BigDecimal(0.0);
        }
        return Response.ok(annualTurnover, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/collectivity/monthly-turnover")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthlyTurnoverCollectivity() {
        List<MonthlyTurnover> result = noteDao.getMonthlyTurnoverCollectivity();
        if (result.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

    //HEADER
    @GET
    @Path("/pms/header/")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPmsNoteHeader() {
        List<TPmsNoteEntete> note = noteDao.getAllPmsNoteHeader();
        if (note.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(note, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/pms/header/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsNoteHeaderById(@PathParam("id") int id) {
        TPmsNoteEntete note = noteDao.getPmsNoteHeaderById(id);        
        if (note == null) {
            throw new NotFoundException();
        }
        return Response.ok(note, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/pms/header/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPmsNoteHeader(TPmsNoteEntete note) {
        try {
            noteDao.addPmsNoteHeader(note);
            return Response.status(Response.Status.CREATED).entity(note).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/pms/header/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePmsNoteHeader(TPmsNoteEntete note) {
        try {
            noteDao.updatePmsNoteHeader(note);
            return Response.status(Response.Status.OK).entity(note).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/pms/header/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePmsNoteHeader(@PathParam("id") Integer id) {
        try {
            noteDao.deletePmsNoteHeader(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }

    // pms/detailed/
    @GET
    @Path("/pms/detailed/")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPmsNoteDetailed() {
        List<TPmsNoteDetail> reservation = noteDao.getAllPmsNoteDetailed();
        if (reservation.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/pms/detailed/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsNoteDetailedById(@PathParam("id") int id) {
        TPmsNoteDetail reservation = noteDao.getPmsNoteDetailedById(id);        
        if (reservation == null) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/pms/detailed/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPmsNoteDetailed(JsonObject pmsReservation) {
        try {
            noteDao.addPmsNoteDetailed(pmsReservation);
            return Response.status(Response.Status.CREATED).entity(pmsReservation).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    
    @Path("/pms/detailed/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePmsNoteDetailed(TPmsNoteDetail pmsSejour) {
        try {
            noteDao.updatePmsNoteDetailed(pmsSejour);
            return Response.status(Response.Status.OK).entity(pmsSejour).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/pms/detailed//{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePmsNoteDetailed(@PathParam("id") int id) {
        try {
            noteDao.deletePmsNoteDetailed(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }
    
    @GET
    @Path("/pms/open-note")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllListCashing(@Context UriInfo info) {
        String dateReference = info.getQueryParameters().getFirst("dateReference");
        BigDecimal openNote = noteDao.openNotesBalances(dateReference);
        if (openNote == null) {
            openNote = new BigDecimal(0.0);
        }
        return Response.ok(openNote, MediaType.APPLICATION_JSON).build();
    }
}
