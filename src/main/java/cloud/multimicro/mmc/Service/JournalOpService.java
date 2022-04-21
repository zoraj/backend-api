/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;
import cloud.multimicro.mmc.Dao.JournalOpDao;
import cloud.multimicro.mmc.Entity.TMmcJournalOperation;
import cloud.multimicro.mmc.Entity.TPosJournalOperation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author herizo
 */
@Stateless
@Path("journal")
@Produces(MediaType.APPLICATION_JSON)
public class JournalOpService {
@Inject
JournalOpDao JournalopDao;


    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByCriteria(JsonObject criteria) {
        List<TMmcJournalOperation> journal = JournalopDao.getByCriteria(criteria);
        if (journal.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(journal, MediaType.APPLICATION_JSON).build();       
    }

//   @GET
//    @Path("")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAll(@Context UriInfo info) {
//        String dateDebut = info.getQueryParameters().getFirst("dateDebut");
//        String dateFin = info.getQueryParameters().getFirst("dateFin");
//        List<TMmcJournalOperation> listModel = JournalopDao.getAll(dateDebut, dateFin);
//        if (listModel.isEmpty()) {
//            throw new NotFoundException();
//        }
//        return Response.ok(listModel, MediaType.APPLICATION_JSON).build();
//    }
    
  
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TPosJournalOperation> journal = JournalopDao.getJournalOperation();
        if (journal.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(journal, MediaType.APPLICATION_JSON).build();       
    }   
    @POST
    @Path("/brigade/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByBrigade(JsonObject criteria1) {
        List<TMmcJournalOperation> journal = JournalopDao.getByBrigade(criteria1);
        if (journal.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(journal, MediaType.APPLICATION_JSON).build();       
    }
    @POST
    @Path("/activite/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByActivity(JsonObject criteria2) {
        List<TMmcJournalOperation> journal = JournalopDao.getByActivity(criteria2);
       /* if (journal.isEmpty()) {
            throw new NotFoundException();
        }*/
        return Response.ok(journal, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/booking/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByBooking(@PathParam("id") Integer id) {
        List<TMmcJournalOperation> journal = JournalopDao.getByBooking(id);
       /* if (journal.isEmpty()) {
            throw new NotFoundException();
        }*/
        return Response.ok(journal, MediaType.APPLICATION_JSON).build();       
    }
    

}
