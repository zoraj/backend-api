/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcImprimante;
import cloud.multimicro.mmc.Entity.TMmcImprimantePrestation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;

/**
 *
 * @author Tsiory
 */
@Stateless
@SuppressWarnings("unchecked")
public class PrinterDao {

    @PersistenceContext
    EntityManager entityManager;

    //GET PRINTER
    public List<TMmcImprimante> getAllPrinter() {
        List<TMmcImprimante> mmcImprimante = entityManager.createQuery("FROM TMmcImprimante  WHERE dateDeletion is null ORDER BY libelle").getResultList();
        return mmcImprimante;
    }

    //GET PRINTER BY ID
    public TMmcImprimante getPrinterById(int id) {
        TMmcImprimante mmcImprimante = entityManager.find(TMmcImprimante.class, id);
        return mmcImprimante;
    }

    //SET PRINTER
    public void setPrinter(TMmcImprimante mmcImprimante) throws CustomConstraintViolationException {
        try {
            entityManager.persist(mmcImprimante);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    //UPDATE PRINTER
    public TMmcImprimante updatePrinter(TMmcImprimante mmcImprimante) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(mmcImprimante);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    //DELETE PRINTER
    public void deletePrinter(int id) {
        entityManager.createNativeQuery("UPDATE t_pms_imprimante SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")              
              .setParameter("id", id)               
              .executeUpdate();
    }

    //GET PRINTER GOUP BY PRODUCT
    public List<TMmcImprimantePrestation> getPrinterProductGroupByProduct() {
        List<TMmcImprimantePrestation> imprimantePrestations = entityManager.createQuery("FROM TMmcImprimantePrestation where posPrestation.dateDeletion is null and mmcImprimante.dateDeletion is null ").getResultList();
        return imprimantePrestations;
    }

    public TMmcImprimantePrestation getPrinterProductByIdProduct(int idProduct) {
        TMmcImprimantePrestation printerProduct = entityManager.find(TMmcImprimantePrestation.class, idProduct);
        return printerProduct;
    }

    //SET PRINTER
    public void setPrinterProduct(JsonObject object) throws CustomConstraintViolationException {
        Integer mmcImprimanteId = Integer.parseInt(object.getString("mmcImprimanteId"));
        String idString = object.getString("posPrestationId");
        List<Integer> idLists = stringToList(idString.substring(1, idString.length() - 1));
          idLists.forEach((Integer id) -> {
             TMmcImprimantePrestation printerProduct = new TMmcImprimantePrestation();
             printerProduct.setMmcImprimanteId(mmcImprimanteId);
             printerProduct.setPosPrestationId(id);
             try {
                persistPrinterProduct(printerProduct);
             } catch (CustomConstraintViolationException ex) {
                 Logger.getLogger(PrinterDao.class.getName()).log(Level.SEVERE, null, ex);
             }
         });
    }

    public void persistPrinterProduct(TMmcImprimantePrestation imprimantePrestation) throws CustomConstraintViolationException {
        try {
             entityManager.persist(imprimantePrestation);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }


    public List<Integer> stringToList(String value) {
        List<Integer> array = new ArrayList<Integer>();
        List<String> arrayString = new ArrayList<String>();
        String str[] = value.split(",");
        arrayString = Arrays.asList(str);
        for (String s : arrayString) {
            array.add(Integer.parseInt(s));
        }
        return array;
    }

    //UPDATE PRINTER
    public void updatePrinterProduct(int idProduct, int printer, int newPrestationGroupeId, int newProductId, int newPrinterId) {
        int updatedEntities = entityManager.createQuery("update TMmcImprimantePrestation SET posPrestationGroupeId =:posPrestationGroupeIdNew, posPrestationId =:posPrestationIdNew, mmcImprimanteId =:mmcImprimanteIdNew WHERE mmcImprimanteId =:printer and posPrestationId =:idProduct")
                .setParameter("posPrestationGroupeIdNew", newPrestationGroupeId)
                .setParameter("posPrestationIdNew", newProductId)
                .setParameter("mmcImprimanteIdNew", newPrinterId)
                .setParameter("printer", printer)
                .setParameter("idProduct", idProduct)
                .executeUpdate();
    }

    //DELETE PRINTER
    public void deletePrinterProduct(int idProduct, int printer) {
        TMmcImprimantePrestation printerProduct = new TMmcImprimantePrestation(idProduct, printer);
        entityManager.remove(entityManager.contains(printerProduct) ? printerProduct : entityManager.merge(printerProduct));
    }
}
