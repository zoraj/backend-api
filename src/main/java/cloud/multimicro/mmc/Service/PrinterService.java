/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;
import cloud.multimicro.mmc.Dao.PrinterDao;
import cloud.multimicro.mmc.Entity.TMmcImprimante;
import cloud.multimicro.mmc.Entity.TMmcImprimantePrestation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
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


/**
 *
 * @author Tsiory
 */
@Stateless
@Path("printer")
@Produces(MediaType.APPLICATION_JSON)
public class PrinterService {
    @Inject
    PrinterDao printerDao;
    
    // GET FAMILY
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImprimante() {
        List<TMmcImprimante> printer = printerDao.getAllPrinter();
        if (printer.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(printer, MediaType.APPLICATION_JSON).build(); 
    }
    
    // GET FAMILY BY ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrinterById(@PathParam("id") int id) {
        TMmcImprimante printer = printerDao.getPrinterById(id);        
        if (printer == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(printer, MediaType.APPLICATION_JSON).build();   
    }
    
    // ADD FAMILY
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPrinter(TMmcImprimante printer){
         try {
             printerDao.setPrinter(printer);
            return Response.status(Response.Status.CREATED).entity(printer).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePrinter(TMmcImprimante printer) {
        try {
            printerDao.updatePrinter(printer);
            return Response.status(Response.Status.OK).entity(printer).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePrinter(@PathParam("id") int id) {
        try {
           printerDao.deletePrinter(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/product")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrinterProduct() {
        List<TMmcImprimantePrestation> printer = printerDao.getPrinterProductGroupByProduct();
        if (printer.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(printer, MediaType.APPLICATION_JSON).build();  
    }
    
    // GET FAMILY BY ID
    @GET
    @Path("/product/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrinterProductByIdProduct(@PathParam("id") int id) {
        TMmcImprimantePrestation printer = printerDao.getPrinterProductByIdProduct(id);        
        if (printer == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(printer, MediaType.APPLICATION_JSON).build();   
    }
    
    // ADD FAMILY
    @Path("/product")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPrinterProduct(JsonObject object) {
        try {
            printerDao.setPrinterProduct(object);
            return Response.status(Response.Status.CREATED).entity(object).build();
        }catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/product/{product-id}/{printer-id}/{product-group-id}/{new-product-id}/{new-printer-id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePrinterProduct(@PathParam("product-id") int idProduct,@PathParam("printer-id") int printer,@PathParam("product-group-id") int productGroupId, @PathParam("new-product-id") int newProductId,@PathParam("new-printer-id") int newPrinterId){
        printerDao.updatePrinterProduct(idProduct, printer, productGroupId, newProductId, newPrinterId);
        return Response.status(Response.Status.OK).entity(idProduct).build();
    }
    
    @Path("/product/{product-id}/{printer-id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePrinterProduct(@PathParam("product-id") int idProduct,@PathParam("printer-id") int printer) {
        try {
           printerDao.deletePrinterProduct(idProduct,printer);
           return Response.ok(idProduct, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }  
}
