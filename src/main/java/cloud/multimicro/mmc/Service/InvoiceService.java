/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.InvoiceDao;
import cloud.multimicro.mmc.Entity.TPmsFacture;
import cloud.multimicro.mmc.Entity.TPmsFactureDetail;
import cloud.multimicro.mmc.Entity.TPosFacture;
import cloud.multimicro.mmc.Entity.TPosFactureDetail;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Naly
 */
@Stateless
@Path("invoice")
@Produces(MediaType.APPLICATION_JSON)
public class InvoiceService {
    
    private static final Logger LOGGER = Logger.getLogger(ProductService.class);
    @Inject
    InvoiceDao invoiceDao;   
    
    /*@GET
    @Path("/pms/invoice-detail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByNumInvoice(@Context UriInfo info) {
        try {
            String numInvoice = info.getQueryParameters().getFirst("numFacture");
            List<VPmsFactureDetail> invoice  = invoiceDao.getInvoiceDetail(numInvoice);
            return Response.ok(invoice, MediaType.APPLICATION_JSON).build();
        }
        catch(NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
        }
    }*/
    
    @GET
    @Path("/pms/invoice-detail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByNumInvoice(@Context UriInfo info) {
        try {
            String numInvoice = info.getQueryParameters().getFirst("numFacture");
            List<TPmsFactureDetail> invoice  = invoiceDao.getInvoiceDetail(numInvoice);
            return Response.ok(invoice, MediaType.APPLICATION_JSON).build();
        }
        catch(NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
        }
    }
    
    @GET
    @Path("/pms/facture")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTpmsFacture() {
         List<TPmsFacture> facture = invoiceDao.getTpmsFacture();        
        if (facture == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(facture, MediaType.APPLICATION_JSON).build();   
    }
    
    @GET
    @Path("/pms/invoice-header")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNumFacture(@Context UriInfo info) {
        try {
            String numInvoice = info.getQueryParameters().getFirst("numFacture");
            TPmsFacture invoice  = invoiceDao.getNumFacture(numInvoice);
            return Response.ok(invoice, MediaType.APPLICATION_JSON).build();
        }
        catch(NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
        }
    }
    
    
    @Path("/pms/facture")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTpmsFacture(TPmsFacture facture) {
        try {
            invoiceDao.setTpmsFacture(facture);
            return Response.status(Response.Status.CREATED).entity(facture).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/pms/facture-detail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTpmsFactureDetail() {
         List<TPmsFactureDetail> facture = invoiceDao.getTpmsFactureDetail();        
        if (facture == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(facture, MediaType.APPLICATION_JSON).build();   
    }
    
    
    @Path("/pms/facture-detail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTpmsFactureDetail(TPmsFactureDetail facture) {
        try {
            invoiceDao.setTpmsFactureDetail(facture);
            return Response.status(Response.Status.CREATED).entity(facture).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/pos/facture")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTposFacture() {
         List<TPosFacture> facture = invoiceDao.getTposFacture();        
        if (facture == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(facture, MediaType.APPLICATION_JSON).build();   
    }
    
    
    @Path("/pos/facture")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTposFacture(TPosFacture facturePos) {
        try {
            invoiceDao.setTposFacture(facturePos);
            return Response.status(Response.Status.CREATED).entity(facturePos).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/pos/facture-detail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTposFactureDetail() {
         List<TPosFactureDetail> facture = invoiceDao.getTposFactureDetail();        
        if (facture == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(facture, MediaType.APPLICATION_JSON).build();   
    }
    
    
    @Path("/pos/facture-detail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTposFactureDetail(TPosFactureDetail facture) {
        try {
            invoiceDao.setTposFactureDetail(facture);
            return Response.status(Response.Status.CREATED).entity(facture).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
   
}
