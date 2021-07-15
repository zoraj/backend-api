package cloud.multimicro.mmc.Service;

import java.text.ParseException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cloud.multimicro.mmc.Dao.SettingDao;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;

import javax.ws.rs.PUT;

/**
 * SettingService
 */
@Stateless
@Path("settings")
@Produces(MediaType.APPLICATION_JSON)
public class SettingService {
    @Inject
    SettingDao settingDao;


    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        var settings = settingDao.getAll();
        if (settings.isEmpty())
            throw new NotFoundException();
        return Response.ok(settings, MediaType.APPLICATION_JSON).build();
    }

    @Path("/{key}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByKey(@PathParam("key") String key) {
        String capitalKey = key.toUpperCase();
        TMmcParametrage setting = settingDao.getSettingByKey(capitalKey);
        if (setting == null) {
            throw new NotFoundException();
        }
        return Response.ok(setting, MediaType.APPLICATION_JSON).build();
    }

    @Path("/{key}/{valeur}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateByKey(@PathParam("key") String key, @PathParam("valeur") String valeur)
            throws DataException, ParseException {
        try {
            String capitalKey = key.toUpperCase();
            settingDao.updateSettingByKey(capitalKey, valeur);
            TMmcParametrage setting = new TMmcParametrage();
            setting.setCle(key);
            setting.setValeur(valeur);
            return Response.status(Response.Status.CREATED).entity(setting).build();
        } catch (CustomConstraintViolationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

}