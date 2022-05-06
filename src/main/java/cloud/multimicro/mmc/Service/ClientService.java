 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.ClientDao;
import cloud.multimicro.mmc.Entity.TMmcClient;
import cloud.multimicro.mmc.Entity.TMmcSegmentClient;
import cloud.multimicro.mmc.Entity.TMmcSociete;
import cloud.multimicro.mmc.Entity.TMmcTypeClient;
import cloud.multimicro.mmc.Entity.TPmsPrescripteur;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 *
 * @author Naly
 */
@Stateless
@Path("clients")
@Produces(MediaType.APPLICATION_JSON)
public class ClientService {
    private static final Logger LOGGER = Logger.getLogger(ClientService.class);
    @Inject
    ClientDao customerDao;
    
    //Societe
    @GET
    @Path("/societe")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSociete() {
        List<TMmcSociete> societe = customerDao.getAllSociete();
        if (societe.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(societe, MediaType.APPLICATION_JSON).build();       
    }
    @GET
    @Path("/societe/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByIdSociete(@PathParam("id") int id) {
        TMmcSociete societe = customerDao.getByIdSociete(id);        
        if (societe == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(societe, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/societe")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSociete(TMmcSociete societe) {
        try {
            customerDao.setSociete(societe);
            return Response.status(Response.Status.CREATED).entity(societe).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    @Path("/societe")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSociete(TMmcSociete societe) {
        try {
            customerDao.updateSociete(societe);
            return Response.status(Response.Status.OK).entity(societe).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    @Path("/societe/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSociete(@PathParam("id") int id) {
        try {
           customerDao.deleteSociete(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    //Client
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TMmcClient> customer = customerDao.getAll();
        if (customer.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(customer, MediaType.APPLICATION_JSON).build();       
    }
    //Client
    @GET
    @Path("/collectivite")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientCollectivite() {
        List<TMmcClient> customer = customerDao.getClientCollectivite();
        if (customer.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(customer, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        TMmcClient customer = customerDao.getByIdCustomer(id);        
        if (customer == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(customer, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addClient(TMmcClient client) {
        try {
            customerDao.setClient(client);
            return Response.status(Response.Status.CREATED).entity(client).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateClient(TMmcClient client) {
        try {
            customerDao.updateClient(client);
            return Response.status(Response.Status.OK).entity(client).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteClient(@PathParam("id") int id) {
        try {
           customerDao.deleteClient(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    // Type client
    @GET
    @Path("/type-client")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypeCLientAll() {
        List<TMmcTypeClient> customer = customerDao.getTypeClientAll(); 
        if (customer.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(customer, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/type-client/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypeClient(@PathParam("id") int id) {
        TMmcTypeClient typeClient = customerDao.getByIdTypeClient(id);        
        if (typeClient == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(typeClient, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/type-client")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTypeClient(TMmcTypeClient typeClient) {
        try {
            customerDao.setTypeClient(typeClient);
            return Response.status(Response.Status.CREATED).entity(typeClient).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/type-client")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTypeClient(TMmcTypeClient typeClient) {
        try {
            customerDao.updateTypeClient(typeClient);
            return Response.status(Response.Status.OK).entity(typeClient).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/type-client/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTypeClient(@PathParam("id") int id) {
        try {
           customerDao.deleteTypeClient(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/segment-client")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSegmentClientAll() {
        List<TMmcSegmentClient> segmentClient = customerDao.getSegmentClientAll();
        if (segmentClient.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(segmentClient, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/segment-client/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSegmentClient(@PathParam("id") int id) {
        TMmcSegmentClient segmentClient = customerDao.getByIdSegmentClient(id);        
        if (segmentClient == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(segmentClient, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/segment-client")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSegmentClient(TMmcSegmentClient segmentClient) {
        try {
            customerDao.setSegmentClient(segmentClient);
            return Response.status(Response.Status.CREATED).entity(segmentClient).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/segment-client")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSegmentClient(TMmcSegmentClient segmentClient) {
        try {
            customerDao.updateSegmentClient(segmentClient);
            return Response.status(Response.Status.OK).entity(segmentClient).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/segment-client/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSegmentClient(@PathParam("id") int id) {
        try {
           customerDao.deleteSegmentClient(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/prescripteur")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPrescripteur() {
        List<TPmsPrescripteur> prescripteur = customerDao.getPrescripteurAll();
        if (prescripteur.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(prescripteur, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/prescripteur/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrescripteur(@PathParam("id") int id) {
        TPmsPrescripteur prescripteur = customerDao.getByIdPrescripteur(id);        
        if (prescripteur == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(prescripteur, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/prescripteur")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPrescripteur(TPmsPrescripteur prescripteur) {
        try {
            customerDao.setPrescripteur(prescripteur);
            return Response.status(Response.Status.CREATED).entity(prescripteur).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/prescripteur")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePrescripteur(TPmsPrescripteur prescripteur) {
        try {
            customerDao.updatePrescripteur(prescripteur);
            return Response.status(Response.Status.OK).entity(prescripteur).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/prescripteur/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePrescripteur(@PathParam("id") int id) {
        try {
           customerDao.deletePrescripteur(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
