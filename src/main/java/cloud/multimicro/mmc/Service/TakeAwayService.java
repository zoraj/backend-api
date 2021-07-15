/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;
import cloud.multimicro.mmc.Dao.TakeAwayDao;
import cloud.multimicro.mmc.Entity.TPosAccompagnement;
import cloud.multimicro.mmc.Entity.TPosCuisson;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Entity.TPosPrestationGroupe;
import cloud.multimicro.mmc.Entity.TPosRevervation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 *
 * @author herizo
 */
@Stateless
@Path("takeway")
public class TakeAwayService {
    @Inject
    TakeAwayDao takeAwayDao;
    
    @Path("/group-products")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductGroup() {
        List<TPosPrestationGroupe> productGroups = takeAwayDao.getProductGroup();
        if (productGroups.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(productGroups, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosProduct() {
        List<TPosPrestation> products = takeAwayDao.getPosProduct();
        if (products.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(products, MediaType.APPLICATION_JSON).build();
    }
    
    // Cuisson
    @GET
    @Path("/cuisson")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCuisson() {
        List<TPosCuisson> cuisson = takeAwayDao.getCuisson();
        if (cuisson.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(cuisson, MediaType.APPLICATION_JSON).build();
    }
    
    // Accompagnement
    @GET
    @Path("/accompagnement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccompagnement() {
        List<TPosAccompagnement> accompagnement = takeAwayDao.getAccompagnement();
        if (accompagnement.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(accompagnement, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/detailscommande")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPosNoteDetailCommande(JsonObject jsonObject) {
        JsonArray listeCommande = jsonObject.getJsonArray("listeCommande");
        for (int i = 0; i < listeCommande.size(); i++) {
            
        try {
                takeAwayDao.addCommandeTakeAway(listeCommande.getJsonObject(i));
                
               
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
        }
    return Response.status(Response.Status.CREATED).entity(listeCommande).build();
    
    }
    
    @Path("/reservation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPosRevervation(TPosRevervation reservation) {
        try {
            takeAwayDao.setPosRevervation(reservation);
            return Response.status(Response.Status.CREATED).entity(reservation).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    
}
