/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.CashSaleDao;
import cloud.multimicro.mmc.Entity.TPmsArrhe;
import cloud.multimicro.mmc.Entity.TPmsVenteComptant;
import cloud.multimicro.mmc.Entity.TPmsVenteComptantDetail;
import cloud.multimicro.mmc.Entity.TPmsVenteComptantEncaissement;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Entity.TPosPrestationGroupe;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
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
import org.jboss.logging.Logger;

/**
 *
 * @author Naly-PC
 */
@Stateless
@Path("cash-sales")
@Produces(MediaType.APPLICATION_JSON)
public class CashSaleService {
    
    private static final Logger LOGGER = Logger.getLogger(ProductService.class);
    @Inject
    CashSaleDao cashSaleDao;
    
    //Vente Comptant
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCashSale() {
        List<TPmsVenteComptant> cashSale = cashSaleDao.getCashSales();
        if (cashSale.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(cashSale, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCashSalesById(@PathParam("id") int id) {
        TPmsVenteComptant cashSale = cashSaleDao.getCashSalesById(id);
        if (cashSale == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(cashSale, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCashSale(TPmsVenteComptant cashSale) {
        try {
            cashSaleDao.setCashSales(cashSale);
            return Response.status(Response.Status.CREATED).entity(cashSale).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCashSale(TPmsVenteComptant cashSale) {
        try {
            cashSaleDao.updateCashSales(cashSale);
            return Response.status(Response.Status.OK).entity(cashSale).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCashSale(@PathParam("id") Integer id) {
        try {
            cashSaleDao.deleteCashSales(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }

    //Vente comptant detail
    @GET
    @Path("/detail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCashSaleDetail() {
        List<TPmsVenteComptantDetail> cashSaleDetail = cashSaleDao.getCashSalesDetail();
        if (cashSaleDetail.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(cashSaleDetail, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/detail")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCashSaleDetail(TPmsVenteComptantDetail cashSaleDetail) {
        try {
            cashSaleDao.setCashSalesDetail(cashSaleDetail);
            return Response.status(Response.Status.CREATED).entity(cashSaleDetail).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/detail")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCashSaleDetail(JsonObject request) {
        try {
           TPmsVenteComptantDetail cashSaleDetail = cashSaleDao.updateCashSaleDetail(request);
            return Response.ok(cashSaleDetail, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    //Vente comptant encaissement
    @GET
    @Path("/encaissement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCashSaleEncaissement() {
        List<TPmsVenteComptantEncaissement> cashSaleEncaissement = cashSaleDao.getCashSalesEncaissement();
        if (cashSaleEncaissement.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(cashSaleEncaissement, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/encaissement")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCashSaleEncaissement(TPmsVenteComptantEncaissement cashSaleEncaissement) {
        try {
            cashSaleDao.setCashSalesEncaissement(cashSaleEncaissement);
            return Response.status(Response.Status.CREATED).entity(cashSaleEncaissement).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/encaissement/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCashSalesEncaissementById(@PathParam("id") int id) {
        TPmsVenteComptantEncaissement cashSale = cashSaleDao.getCashSalesEncaissementById(id);
        if (cashSale == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(cashSale, MediaType.APPLICATION_JSON).build();
    }
    
}