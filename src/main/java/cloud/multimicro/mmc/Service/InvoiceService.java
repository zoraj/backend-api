/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.InvoiceDao;
import cloud.multimicro.mmc.Entity.VPmsEditionDepartPrevu;
import cloud.multimicro.mmc.Entity.VPmsFacture;
import cloud.multimicro.mmc.Entity.VPmsFactureDetail;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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
    
    @GET
    @Path("/pms/invoice-header")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInvoiceHeader(@Context UriInfo info) {
        try {
            String numInvoice = info.getQueryParameters().getFirst("numFacture");
            VPmsFacture invoice  = invoiceDao.getInvoiceHeader(numInvoice);
            return Response.ok(invoice, MediaType.APPLICATION_JSON).build();
        }
        catch(NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
        }
    }
    
    @GET
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
    }
    
    @GET
    @Path("/pms/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByEnteteId(@PathParam("id") int id) {
        VPmsFacture invoice = invoiceDao.getByEnteteId(id);
        if (invoice == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(invoice, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/pms")  
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@Context UriInfo info) {        
        List<VPmsFacture> invoiceList = invoiceDao.getAll(info.getQueryParameters());
        if (invoiceList.isEmpty()) {
            throw new NotFoundException();
        } 
        return Response.ok(invoiceList, MediaType.APPLICATION_JSON).build();
    }
}
