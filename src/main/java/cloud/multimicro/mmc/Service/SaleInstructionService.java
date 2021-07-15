/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.SaleInstructionDao;
import cloud.multimicro.mmc.Entity.TPmsConsigneVente;
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

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("sales-instructions")
@Produces(MediaType.APPLICATION_JSON)
public class SaleInstructionService {
    @Inject
    SaleInstructionDao saleInstructionDao;
    
    // GET FAMILY
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<TPmsConsigneVente> resultList = saleInstructionDao.getAll();
        if (resultList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(resultList, MediaType.APPLICATION_JSON).build();
    }

    // GET FAMILY BY ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFamilyById(@PathParam("id") Integer id) {
        TPmsConsigneVente result = saleInstructionDao.getById(id);
        if (result == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

    // ADD FAMILY
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response set(TPmsConsigneVente value) {
        try {
            saleInstructionDao.set(value);
            return Response.status(Response.Status.CREATED).entity(value).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(TPmsConsigneVente value) {
        try {
            saleInstructionDao.update(value);
            return Response.status(Response.Status.OK).entity(value).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        try {
            saleInstructionDao.delete(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
