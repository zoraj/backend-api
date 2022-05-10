/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.FamilyDao;
import cloud.multimicro.mmc.Entity.TMmcFamilleCa;
import cloud.multimicro.mmc.Entity.TMmcSousFamilleCa;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("families")
@Produces(MediaType.APPLICATION_JSON)
public class FamilyService {
    @Inject
    FamilyDao familyDao;

    // GET FAMILY
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFamilies() {
        List<TMmcFamilleCa> families = familyDao.getFamily();
        if (families.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(families, MediaType.APPLICATION_JSON).build();
    }

    // GET FAMILY BY ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFamilyById(@PathParam("id") int id) {
        TMmcFamilleCa family = familyDao.getFamilyById(id);
        if (family == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(family, MediaType.APPLICATION_JSON).build();
    }

    // ADD FAMILY
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFamily(TMmcFamilleCa family) {
        try {
            familyDao.setFamily(family);
            return Response.status(Response.Status.CREATED).entity(family).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFamily(TMmcFamilleCa family) {
        try {
            familyDao.updateFamily(family);
            return Response.status(Response.Status.OK).entity(family).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFamily(@PathParam("id") int id) {
        try {
            familyDao.deleteFamily(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // GET SUB FAMILY
    @Path("/sub-families")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubFamily() {
        List<TMmcSousFamilleCa> subFamily = familyDao.getSubFamily();
        if (subFamily.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(subFamily, MediaType.APPLICATION_JSON).build();
    }

    // GET SUB FAMILY
    @Path("/sub-families/nature")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSubFamily(@Context UriInfo info) {
        MultivaluedMap<String, String> parameters = info.getQueryParameters();
        String nature = parameters.getFirst("nature");
        List<TMmcSousFamilleCa> subFamily = familyDao.getAllSubFamily(nature);
        if (subFamily.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(subFamily, MediaType.APPLICATION_JSON).build();
    }

    // GET SUB FAMILY BY ID
    @GET
    @Path("/sub-families/by-family/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubFamilyByFamily(@PathParam("id") Integer id) {
        List<TMmcSousFamilleCa> subFamily = familyDao.getSubFamilyByFamily(id);
        if (subFamily.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(subFamily, MediaType.APPLICATION_JSON).build();
    }

    // GET SUB FAMILY BY ID
    @GET
    @Path("/sub-families/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubFamilyById(@PathParam("id") int id) {
        TMmcSousFamilleCa subFamily = familyDao.getSubFamilyById(id);
        if (subFamily == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(subFamily, MediaType.APPLICATION_JSON).build();
    }

    // ADD SUB FAMILY
    @Path("/sub-families")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSubFamily(TMmcSousFamilleCa subFamily) {
        try {
            familyDao.setSubFamily(subFamily);
            return Response.status(Response.Status.CREATED).entity(subFamily).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/sub-families")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSubFamily(TMmcSousFamilleCa subFamily) {
        try {
            familyDao.updateSubFamily(subFamily);
            return Response.status(Response.Status.OK).entity(subFamily).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/sub-families/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSubFamily(@PathParam("id") int id) {
        try {
            familyDao.deleteSubFamily(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
