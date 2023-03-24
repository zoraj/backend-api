/* 
 * Copyright (C) MultiMicro Cloud, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zo Rajaonarivony <zo@multimicro.fr>, November 2019
 * 
 */
package cloud.multimicro.mmc.Service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import cloud.multimicro.mmc.Dao.RateDao;
import cloud.multimicro.mmc.Dao.SettingDao;
import cloud.multimicro.mmc.Entity.TPmsCategorieTarif;
import cloud.multimicro.mmc.Entity.TPmsModelTarif;
import cloud.multimicro.mmc.Entity.TPmsModelTarifDetail;
import cloud.multimicro.mmc.Entity.TPmsTarifOption;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import javax.json.JsonObject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

/**
 *
 * @author Tsiory
 */
@Stateless
@Path("rates")
@Produces(MediaType.APPLICATION_JSON)
public class RateService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class);
    
    @Inject
    RateDao rateDao;
    
    @Inject
    SettingDao settingDao;

    //PMS MODEL TARIF
    @GET
    @Path("/models")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsFareModel() {
        List<TPmsModelTarif> model = rateDao.getPmsFareModel();
        if (model.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(model, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/models/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsFareModelById(@PathParam("id") int id) {
        TPmsModelTarif model = rateDao.getPmsFareModelById(id);
        if (model == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(model, MediaType.APPLICATION_JSON).build();
    }

    @Path("/models")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFareModel(TPmsModelTarif pmsModelTarif) {
        try {
            rateDao.updatePmsFareModel(pmsModelTarif);
            return Response.status(Response.Status.OK).entity(pmsModelTarif).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }

    @Path("/models")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPmsFareModel(TPmsModelTarif pmsModelTarif) {
        try {
            pmsModelTarif.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            rateDao.setPmsFareModel(pmsModelTarif);
            return Response.status(Response.Status.CREATED).entity(pmsModelTarif).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/models/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFareModel(@PathParam("id") int id) {
        try {
            rateDao.deletePmsFareModel(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    //PMS CATEGORIE TARIF
    @GET
    @Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsFareCategory() {
        List<TPmsCategorieTarif> tarif = rateDao.getPmsFareCategory();
        if (tarif.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(tarif, MediaType.APPLICATION_JSON).build();
    }

    @Path("/categories")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePmsFareCategory(TPmsCategorieTarif pmsCategorieTarif) {
        try {
            rateDao.updateFareCategory(pmsCategorieTarif);
            return Response.status(Response.Status.OK).entity(pmsCategorieTarif).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/categories/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsFareCategoryById(@PathParam("id") int id) {
        TPmsCategorieTarif pmsCategorieTarifs = rateDao.getPmsFareCategoryById(id);
        if (pmsCategorieTarifs == null) {
            throw new NotFoundException();
        }
        return Response.ok(pmsCategorieTarifs, MediaType.APPLICATION_JSON).build();
    }

    @Path("/categories")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPmsFareCategory(TPmsCategorieTarif pmsCategorieTarif) {
        try {
            rateDao.setPmsFareCategory(pmsCategorieTarif);
            return Response.status(Response.Status.CREATED).entity(pmsCategorieTarif).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/categories/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFareCategory(@PathParam("id") int id) {
        try {
            rateDao.deletePmsFareCategory(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/models/details/{modelId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsFareModelDetailedById(@PathParam("modelId") int id) {
        List<TPmsModelTarifDetail> details = rateDao.getPmsFareModelDetailedByModelId(id);
        if (details.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(details, MediaType.APPLICATION_JSON).build();
    }

    @Path("/models/details/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPmsFareModelDetailed(TPmsModelTarifDetail pmsModelTarifDetail) {
         try {
            rateDao.setPmsFareModelDetailed(pmsModelTarifDetail);
            return Response.status(Response.Status.CREATED).entity(pmsModelTarifDetail).build();
        }
        catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/models/details/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFareModelDetailed(@PathParam("id") int id) {
        try {
            rateDao.deletePmsModelTarifDetail(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/pms/fare-model-detailed/by-fare-model")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPmsModelTarifDetail(JsonObject request) {
         try {
            rateDao.addPmsModelTarifDetail(request);
            return Response.status(Response.Status.OK).entity(request).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
      
    }

    @Path("/options")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTarifOption() {
        List<TPmsTarifOption> tarifOption = rateDao.getAllTarifOption();
        if (tarifOption == null) {
            throw new NotFoundException();
        }
        return Response.ok(tarifOption, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/options/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTarifOptionById(@PathParam("id") int id) {
        TPmsTarifOption model = rateDao.getTarifOptionById(id);
        if (model == null) {
            throw new NotFoundException();
            //return Response.status(Response.Status.NOT_FOUND).entity("Object not found.").build();  
        }
        return Response.ok(model, MediaType.APPLICATION_JSON).build();
    }

    @Path("/options")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTarifOption(TPmsTarifOption tarifOption) {
        try {
            rateDao.updateTarifOption(tarifOption);
            return Response.status(Response.Status.OK).entity(tarifOption).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/options")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setTarifOption(TPmsTarifOption tarifOption) {
        try {
            rateDao.setTarifOption(tarifOption);
            return Response.status(Response.Status.OK).entity(tarifOption).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/options/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTarifOption(@PathParam("id") int id) {
        boolean isExist = rateDao.deleteTarifOption(id);
        if (isExist == true) {
            throw new BadRequestException();
        }
        return Response.ok(isExist, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pms/rate-option-by-rate-model/{model-rate-id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModelTarifOptionByIdModelTarif(@PathParam("model-rate-id") int id) {
        List<TPmsTarifOption> tarifModel = rateDao.getModelTarifOptionByIdModelTarif(id);
        if (tarifModel.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(tarifModel, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pms/model-rate-option")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addModelTarifOption(JsonObject jsonObject) { 
         try {
            rateDao.addModelTarifOption(jsonObject);
            return Response.status(Response.Status.OK).entity(jsonObject).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
