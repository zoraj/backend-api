/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.VaeDao;
import cloud.multimicro.mmc.Entity.TPosClientVae;

import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Entity.TPosRevervation;
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
import javax.ws.rs.Consumes;
import javax.naming.AuthenticationException;

/**
 *
 * @author herizo
 */
@Stateless
@Path("vae")
@Produces(MediaType.APPLICATION_JSON)
public class VaeService {
    private static final Logger LOGGER = Logger.getLogger(VaeService.class);
    @Inject
    VaeDao VaeDao;

    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TPosClientVae> vente = VaeDao.getAll();
        if (vente.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(vente, MediaType.APPLICATION_JSON).build();       
    }
    @GET
    @Path("/reservation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosRevervation() {
        List<TPosRevervation> reservation = VaeDao.getAllPosRevervation();
        if (reservation.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();       
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        TPosClientVae vente = VaeDao.getByIdCustomer(id);        
        if (vente == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(vente, MediaType.APPLICATION_JSON).build();   
    }
    
    @GET
    @Path("/reservation/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByIdPosRevervation(@PathParam("id") int id) {
        TPosRevervation reservation = VaeDao.getByIdPosRevervation(id);        
        if (reservation == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(reservation, MediaType.APPLICATION_JSON).build();   
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(TPosClientVae vente) {
        try {
            TPosClientVae newVente = VaeDao.create(vente);
            //VaeDao.setClient(vente);
            return Response.status(Response.Status.CREATED).entity(newVente).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/reservation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPosRevervation(TPosRevervation reservation) {
        try {
            VaeDao.setPosRevervation(reservation);
            return Response.status(Response.Status.CREATED).entity(reservation).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
//    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateClient(TPosClientVae vente) {
        try {
            VaeDao.updateClient(vente);
            return Response.status(Response.Status.OK).entity(vente).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/reservation")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePosRevervation(TPosRevervation reservation) {
        try {
            VaeDao.updatePosRevervation(reservation);
            return Response.status(Response.Status.OK).entity(reservation).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
//    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteClient(@PathParam("id") int id) {
        try {
           VaeDao.deleteClient(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/reservation/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePosRevervation(@PathParam("id") int id) {
        try {
           VaeDao.deletePosRevervation(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/signin")
    @Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
    public Response checkCredentials(TPosClientVae u) throws AuthenticationException {
        String email = u.getEmail();
        String pass = u.getPass();
        TPosClientVae user = VaeDao.checkCredentials(email, pass);
        
        if (user == null) {
            throw new AuthenticationException();
        }
        // Return a JWT token if everything is ok
        //String token = Jwt.generateToken();
        //user.setToken(token);
        return Response.ok(user, MediaType.APPLICATION_JSON).build();  
    }

    @Path("/products/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosProductByIdGroupVae(@PathParam("id") int id) {
        List<TPosPrestation> products = VaeDao.getPosProductByIdGroupVae(id);
        if (products == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(products, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductOrderVae() {
        List<TPosPrestation> products = VaeDao.getProductOrderVae();
        if (products.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(products, MediaType.APPLICATION_JSON).build();
    }
    
}
