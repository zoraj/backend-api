/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Service;

import cloud.multimicro.mmc.Dao.StockOtherDao;
import cloud.multimicro.mmc.Entity.TPmsReservationStockAutre;
import cloud.multimicro.mmc.Entity.TPmsStockAutre;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import java.text.ParseException;
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
 * @author Tsiory-PC
 */
@Stateless
@Path("stock-other")
@Produces(MediaType.APPLICATION_JSON)
public class StockOtherService {
    
    private static final Logger LOGGER = Logger.getLogger(ProductService.class);
    @Inject
    StockOtherDao stockOtherDaoDao;
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStockOther() {
        List<TPmsStockAutre> stock = stockOtherDaoDao.getAllStockOther();
        if (stock.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(stock, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStockOtherId(@PathParam("id") int id) {
        TPmsStockAutre stock = stockOtherDaoDao.getStockOtherId(id);
        if (stock == null) {
            throw new NotFoundException();
        }
        return Response.ok(stock, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setStockOther(TPmsStockAutre stock) {
        try {
            stockOtherDaoDao.setStockOther(stock);
            return Response.status(Response.Status.CREATED).entity(stock).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStockOther(TPmsStockAutre stock) {
        try {
            stockOtherDaoDao.updateStockOther(stock);
            return Response.status(Response.Status.OK).entity(stock).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStockOther(@PathParam("id") Integer id) {
        try {
            stockOtherDaoDao.deleteStockOther(id);
           return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }
    
    @GET
    @Path("/reservation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllResaStockOther() {
        List<TPmsReservationStockAutre> stockResa = stockOtherDaoDao.getAllResaStockOther();
        if (stockResa.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(stockResa, MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("/reservation/{idReservation}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResaStockOtherByIdReservation(@PathParam("idReservation") int idReservation) {
        List<TPmsReservationStockAutre> stockResa = stockOtherDaoDao.getResaStockOtherByIdReservation(idReservation);
        if (stockResa == null) {
            throw new NotFoundException();
        }
        return Response.ok(stockResa, MediaType.APPLICATION_JSON).build();
    }
    
    @Path("/reservation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setResaStockOther(List<TPmsReservationStockAutre> stockResa) throws DataException, ParseException  {
        try {
            for(int i = 0; i < stockResa.size(); i++){
                stockOtherDaoDao.setResaStockOther(stockResa.get(i));
            }
            return Response.status(Response.Status.CREATED).entity(stockResa).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("/reservation")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateResaStockOther(TPmsReservationStockAutre stockResa) {
        try {
            stockOtherDaoDao.updateResaStockOther(stockResa);
            return Response.status(Response.Status.OK).entity(stockResa).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @Path("/reservation/{idReservation}/{idStockAutre}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteResaStockOther(@PathParam("idReservation") Integer idReservation, @PathParam("idStockAutre") Integer idStockAutre) {
        try {
            stockOtherDaoDao.deleteResaStockOther(idReservation, idStockAutre);
           return Response.ok(idReservation, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }   
    }
}