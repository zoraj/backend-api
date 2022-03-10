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
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import cloud.multimicro.mmc.Dao.ProductDao;
import cloud.multimicro.mmc.Entity.TPmsPrestation;
import cloud.multimicro.mmc.Entity.TPosAccompagnement;
import cloud.multimicro.mmc.Entity.TPosActivitePrestation;
import cloud.multimicro.mmc.Entity.TPosCuisson;
import cloud.multimicro.mmc.Entity.TPosHappyHours;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import javax.ws.rs.POST;
import cloud.multimicro.mmc.Entity.TPosPrestationGroupe;
import cloud.multimicro.mmc.Entity.TPosSiropParfum;
import cloud.multimicro.mmc.Entity.TPosSiropParfumCategorie;
import cloud.multimicro.mmc.Entity.TPosTarif;
import cloud.multimicro.mmc.Entity.TPosTarifPrestation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import java.math.BigDecimal;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;

/**
 * ProductService
 */
@Stateless
@Path("products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class);
    @Inject
    ProductDao productDao;

    @GET
    @Path("/pos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosProduct() {
        List<TPosPrestation> products = productDao.getPosProduct();
        if (products.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(products, MediaType.APPLICATION_JSON).build();
    }

    // @GET
    // @Path("/pos/nature/{admission}/{subvention}/{client}")
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response getPosNatureProduct(@PathParam("admission") String admission,
    // @PathParam("subvention") String subvention, @PathParam("client") int client)
    // {
    // List<TPosPrestation> products = productDao.getByNature(admission,
    // subvention,client);
    // if (products.isEmpty()) {
    // throw new NotFoundException();
    // }
    // return Response.ok(products, MediaType.APPLICATION_JSON).build();
    // }

    // @Path("/pos/nature")
    // @PUT
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response updateByNature(TPosPrestation products) {
    // try {
    // productDao.updateByNature(products);
    // return Response.status(Response.Status.OK).entity(products).build();
    // } catch (Exception e) {
    // return
    // Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    // }
    // }
    @Path("/pos/by-group-vae/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosProductByIdGroupVae(@PathParam("id") int id) {
        List<TPosPrestation> products = productDao.getPosProductByIdGroupVae(id);
        if (products == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(products, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/order-vae")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductOrderVae() {
        List<TPosPrestation> products = productDao.getProductOrderVae();
        if (products.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(products, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPos(@PathParam("id") int id) {
        TPosPrestation product = productDao.getPosProductById(id);
        if (product == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(product, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPosPrestation(TPosPrestation prestation) {
        try {
            productDao.setPosPrestation(prestation);
            return Response.status(Response.Status.CREATED).entity(prestation).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/pos/{id}/{gestion}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response gestionNonFacture(@PathParam("id") int id, @PathParam("gestion") String gestion) {
        try {
            productDao.gestionTypeNonFacture(id, gestion);
            return Response.status(Response.Status.OK).entity(id).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/pos")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePosPrestation(TPosPrestation prestation) {
        try {
            productDao.updatePosPrestation(prestation);
            return Response.status(Response.Status.OK).entity(prestation).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/pos/by-group/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosPrestationByIdGroup(@PathParam("id") int id) {
        List<TPosPrestation> products = productDao.getPosProductByIdGroup(id);
        if (products == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(products, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePosPrestation(@PathParam("id") Integer id) {
        try {
            productDao.deletePosPrestation(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (DataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }

    // Groupe prestation
    @GET
    @Path("/groups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductGroup() {
        List<TPosPrestationGroupe> products = productDao.getProductGroup();
        if (products.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(products, MediaType.APPLICATION_JSON).build();
    }
    
    

    @GET
    @Path("/groups/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductGroupById(@PathParam("id") int id) {
        TPosPrestationGroupe productGroupe = productDao.getProductGroupById(id);
        if (productGroupe == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(productGroupe, MediaType.APPLICATION_JSON).build();
    }

    @Path("/groups")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProductGroup(TPosPrestationGroupe prestationGroupe) {
        try {
            productDao.setProductGroup(prestationGroupe);
            return Response.status(Response.Status.CREATED).entity(prestationGroupe).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/groups")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductGroup(TPosPrestationGroupe prestationGroupe) {
        try {
            productDao.updateProductGroup(prestationGroupe);
            return Response.status(Response.Status.OK).entity(prestationGroupe).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/groups/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProductGroup(@PathParam("id") Integer id) {
        try {
            productDao.deleteProductGroup(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (DataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // PMS
    @GET
    @Path("/pms")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsProduct() {
        List<TPmsPrestation> products = productDao.getPmsProduct();
        if (products.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(products, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pms")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(TPmsPrestation prestation) {
        try {
            productDao.updateProduct(prestation);
            return Response.status(Response.Status.OK).entity(prestation).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/pms/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductStop(@PathParam("id") Integer id) {
        try {
            productDao.updateProductStop(id);
            return Response.status(Response.Status.CREATED).entity(id).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/pms/avail/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductAvail(@PathParam("id") Integer id) {
        try {
            productDao.updateProductAvail(id);
            return Response.status(Response.Status.CREATED).entity(id).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/pms/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPmsProductById(@PathParam("id") int id) {
        TPmsPrestation product = productDao.getPmsProductById(id);
        if (product == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(product, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pms")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(TPmsPrestation prestation) {
        try {
            productDao.setProduct(prestation);
            return Response.status(Response.Status.CREATED).entity(prestation).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/pms/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoomCategorie(@PathParam("id") int id) {
        try {
            productDao.deletePmsProduct(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // POS Sirop parfum
    @GET
    @Path("/sirup-parfume")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSirupParfum() {
        List<TPosSiropParfum> sirupParfumes = productDao.getSiropParfum();
        if (sirupParfumes.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(sirupParfumes, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/sirup-parfume/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSiropParfum(@PathParam("id") int id) {
        TPosSiropParfum siropParfum = productDao.getPosSiropParfumById(id);
        if (siropParfum == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(siropParfum, MediaType.APPLICATION_JSON).build();
    }

    @Path("/sirup-parfume")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSirupParfum(TPosSiropParfum sirupParfum) {
        try {
            productDao.setSiropParfum(sirupParfum);
            return Response.status(Response.Status.CREATED).entity(sirupParfum).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/sirup-parfume")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSirupParfum(TPosSiropParfum sirupParfum) {
        try {
            productDao.updateSiropParfum(sirupParfum);
            return Response.status(Response.Status.OK).entity(sirupParfum).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/sirup-parfume/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSiropParfum(@PathParam("id") int id) {
        try {
            productDao.deleteSiropParfum(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Catégorie
    @GET
    @Path("/categorie")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategorie() {
        List<TPosSiropParfumCategorie> categorie = productDao.getSpCategorie();
        if (categorie.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(categorie, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/categorie/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategorieById(@PathParam("id") int id) {
        TPosSiropParfumCategorie spCategorie = productDao.getPosSiropParfumCategorieById(id);
        if (spCategorie == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(spCategorie, MediaType.APPLICATION_JSON).build();
    }

    @Path("/categorie")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCategorie(TPosSiropParfumCategorie categorie) {
        try {
            productDao.setSpCategorie(categorie);
            return Response.status(Response.Status.CREATED).entity(categorie).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/categorie")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategorie(TPosSiropParfumCategorie categorie) {
        try {
            productDao.updateSpCategorie(categorie);
            return Response.status(Response.Status.OK).entity(categorie).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/categorie/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSpCategorie(@PathParam("id") int id) {
        try {
            productDao.deleteSpCategorie(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Accompagnement
    @GET
    @Path("/accompagnement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccompagnement() {
        List<TPosAccompagnement> accompagnement = productDao.getAccompagnement();
        if (accompagnement.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(accompagnement, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/accompagnement/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccompagnementById(@PathParam("id") int id) {
        TPosAccompagnement accompagnement = productDao.getAccompagnementById(id);
        if (accompagnement == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(accompagnement, MediaType.APPLICATION_JSON).build();
    }

    @Path("/accompagnement")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAccompagnement(TPosAccompagnement accompagnement) {
        try {
            productDao.setAccompagnement(accompagnement);
            return Response.status(Response.Status.CREATED).entity(accompagnement).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/accompagnement")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAccompagnement(TPosAccompagnement accompagnement) {
        try {
            productDao.updateAccompagnement(accompagnement);
            return Response.status(Response.Status.OK).entity(accompagnement).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/accompagnement/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAccompagnement(@PathParam("id") int id) {
        try {
            productDao.deleteAccompagnement(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Cuisson
    @GET
    @Path("/cuisson")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCuisson() {
        List<TPosCuisson> cuisson = productDao.getCuisson();
        if (cuisson.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(cuisson, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/cuisson/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCuissonById(@PathParam("id") int id) {
        TPosCuisson cuisson = productDao.getCuissonById(id);
        if (cuisson == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(cuisson, MediaType.APPLICATION_JSON).build();
    }

    @Path("/cuisson")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCuisson(TPosCuisson cuisson) {
        try {
            productDao.setCuisson(cuisson);
            return Response.status(Response.Status.CREATED).entity(cuisson).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/cuisson")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCuisson(TPosCuisson cuisson) {
        try {
            productDao.updateCuisson(cuisson);
            return Response.status(Response.Status.OK).entity(cuisson).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/cuisson/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCuisson(@PathParam("id") int id) {
        try {
            productDao.deleteCuisson(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // TARIF
    @GET
    @Path("/pos/tarif")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosTarif() {
        List<TPosTarif> tarif = productDao.getPosTarif();
        if (tarif.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(tarif, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/tarif/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosTarifById(@PathParam("id") int id) {
        TPosTarif tarif = productDao.getPosTarifById(id);
        if (tarif == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(tarif, MediaType.APPLICATION_JSON).build();
    }

    @Path("/pos/tarif")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPosTarif(TPosTarif tarif) {
        try {
            productDao.setPosTarif(tarif);
            return Response.status(Response.Status.CREATED).entity(tarif).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/pos/tarif")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCuisson(TPosTarif tarif) {
        try {
            productDao.updatePosTarif(tarif);
            return Response.status(Response.Status.OK).entity(tarif).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/pos/tarif/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePosTarif(@PathParam("id") int id) {
        try {
            productDao.deletePosTarif(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // POS Happy Hours
    @GET
    @Path("/happyHours")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHappyHours() {
        List<TPosHappyHours> happyHours = productDao.getHappyHours();
        if (happyHours.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(happyHours, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/happyHours/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHappyHours(@PathParam("id") int id) {
        TPosHappyHours happyHours = productDao.getHappyHoursById(id);
        if (happyHours == null) {
            throw new NotFoundException();
            // return Response.status(Response.Status.NOT_FOUND).entity("Object not
            // found.").build();
        }
        return Response.ok(happyHours, MediaType.APPLICATION_JSON).build();
    }

    @Path("/happyHours")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addHappyHours(TPosHappyHours happyHours) {
        try {
            productDao.setHappyHours(happyHours);
            return Response.status(Response.Status.CREATED).entity(happyHours).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/happyHours/{id}/{active}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response activeHappyHours(@PathParam("id") int id, @PathParam("active") boolean active) {
        try {
            productDao.activeHappyHours(id, active);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/happyHours")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateHappyHours(TPosHappyHours happyHours) {
        try {
            productDao.updateHappyHours(happyHours);
            return Response.status(Response.Status.OK).entity(happyHours).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/happyHours/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteHappyHours(@PathParam("id") int id) {
        try {
            productDao.deleteHappyHours(id);
            return Response.ok(id, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // postarifprestation
    @GET
    @Path("/tarifPrestation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTarifPrestation() {
        List<TPosTarifPrestation> tarifPresta = productDao.getAllTarifPrestation();
        if (tarifPresta.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(tarifPresta, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/tarifPrestation/{product-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTarifPrestationById(@PathParam("product-id") int productId) {
        List<TPosTarifPrestation> tarifPresta = productDao.getTarifPrestationById(productId);
        if (tarifPresta.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(tarifPresta, MediaType.APPLICATION_JSON).build();
    }

    @Path("/tarifPrestation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTarifPrestation(TPosTarifPrestation tarifPresta) {
        try {
            productDao.setTarifPrestation(tarifPresta);
            return Response.status(Response.Status.CREATED).entity(tarifPresta).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("/tarifPrestation/{product-id}/{tarif-id}/{new-product-id}/{new-tarif-id}/{montant}/{prixMinimum}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTarifPrestation(@PathParam("product-id") int productId, @PathParam("tarif-id") int tarifId,
            @PathParam("new-product-id") int newProductId, @PathParam("new-tarif-id") int newTarifId,
            @PathParam("montant") BigDecimal montant, @PathParam("prixMinimum") BigDecimal prixMinimum) {
        try {
            productDao.updateTarifPrestation(productId, tarifId, newProductId, newTarifId, montant, prixMinimum);
            return Response.ok(productId, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/tarifPrestation/{idTarif}/{idProduct}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTarifPrestation(@PathParam("idTarif") int idTarif, @PathParam("idProduct") int idProduct) {
        try {
            productDao.deleteTarifPrestation(idTarif, idProduct);
            return Response.ok(idProduct, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/pos/order-kitchen")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductOrderKitchen() {
        List<TPosPrestation> products = productDao.getProductOrderKitchen();
        if (products.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(products, MediaType.APPLICATION_JSON).build();
    }

    // recuperation des subvention et admission par client
    @GET
    @Path("/pos/{nature}/{mmcSocieteId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductByNatureBySociety(@PathParam("nature") String nature,
            @PathParam("mmcSocieteId") int mmcSocieteId) {
        List<TPosPrestation> posPrestationList = productDao.getProductByNatureBySociety(nature, mmcSocieteId);
        if (posPrestationList.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(posPrestationList, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/pos/nature-by-id-sous-famille/{id-sousfamille}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNatureByIdSousFamille(@PathParam("id-sousfamille") int idSousFamille) {
        try {
            boolean nature = productDao.getNatureByIdSousFamille(idSousFamille);
            return Response.ok(nature, MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("/activity/{product-id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActiviteProductByIdProduct(@PathParam("product-id") int id) {
        List<TPosActivitePrestation> results = productDao.getActiviteProductByIdProduct(id);
        if (results.isEmpty()) {
            throw new NotFoundException();
        }
        return Response.ok(results, MediaType.APPLICATION_JSON).build();
    }

    @Path("/activity")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addActiviteProduct(JsonObject jsonObject) {
        try {
            productDao.addActiviteProduct(jsonObject);
            return Response.status(Response.Status.OK).entity(jsonObject).build();
        } catch (CustomConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
