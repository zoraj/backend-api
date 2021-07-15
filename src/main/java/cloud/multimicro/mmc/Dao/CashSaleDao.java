/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsArrhe;
import cloud.multimicro.mmc.Entity.TPmsPrestation;
import cloud.multimicro.mmc.Entity.TPmsVenteComptant;
import cloud.multimicro.mmc.Entity.TPmsVenteComptantDetail;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Naly-PC
 */
@Stateless
@SuppressWarnings("unchecked")
public class CashSaleDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    //Vente comptant
    public List<TPmsVenteComptant> getCashSales() {
        List<TPmsVenteComptant> cashSales = entityManager.createQuery("FROM TPmsVenteComptant  WHERE dateDeletion = null")
                .getResultList();
        return cashSales;
    }
    
    public TPmsVenteComptant getCashSalesById(int id) {
        TPmsVenteComptant cashSales = entityManager.find(TPmsVenteComptant.class, id);
        return cashSales;
    }

    public void setCashSales(TPmsVenteComptant cashSales) throws CustomConstraintViolationException{
         try {
        entityManager.persist(cashSales);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TPmsVenteComptant updateCashSales(TPmsVenteComptant cashSales) throws CustomConstraintViolationException {
        try {
        return entityManager.merge(cashSales);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
        
    }
    
    public void deleteCashSales(int id) throws CustomConstraintViolationException{
        TPmsVenteComptant cashSales = getCashSalesById(id);
        Date dateDel = new Date();
        cashSales.setDateDeletion(dateDel);
        try {
        entityManager.merge(cashSales);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    //Vente comptant detail
    public List<TPmsVenteComptantDetail> getCashSalesDetail() {
        List<TPmsVenteComptantDetail> cashSalesDetail = entityManager.createQuery("FROM TPmsVenteComptantDetail  WHERE dateDeletion = null")
                .getResultList();
        return cashSalesDetail;
    }
    
    public TPmsVenteComptantDetail getCashSalesDetailById(int id) {
        TPmsVenteComptantDetail cashSalesDetail = entityManager.find(TPmsVenteComptantDetail.class, id);
        return cashSalesDetail;
    }
    
    public void setCashSalesDetail(TPmsVenteComptantDetail cashSalesDetail) throws CustomConstraintViolationException {
        try {
            entityManager.persist(cashSalesDetail);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TPmsVenteComptantDetail updateCashSaleDetail(JsonObject request) throws CustomConstraintViolationException{

        TPmsVenteComptantDetail venteComptantDetail = getCashSalesDetailById(Integer.parseInt(request.get("id").toString()));
        TPmsVenteComptantDetail venteComptantDetailUpdate = new TPmsVenteComptantDetail();
        
        venteComptantDetailUpdate.setPmsVenteComptantId(venteComptantDetail.getPmsVenteComptantId());
        venteComptantDetailUpdate.setPmsPrestationId(venteComptantDetail.getPmsPrestationId());
        venteComptantDetailUpdate.setQte(venteComptantDetail.getQte()*(-1));
        venteComptantDetailUpdate.setPu(venteComptantDetail.getPu());        
        venteComptantDetailUpdate.setRemise(venteComptantDetail.getRemise());
        venteComptantDetailUpdate.setCommission(venteComptantDetail.getCommission());
        venteComptantDetailUpdate.setOrigine(venteComptantDetail.getOrigine());
        try {
            entityManager.persist(venteComptantDetailUpdate);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
        
        TPmsVenteComptantDetail newVenteComptantDetail = new TPmsVenteComptantDetail();

        newVenteComptantDetail.setPmsVenteComptantId(venteComptantDetail.getPmsVenteComptantId());
        newVenteComptantDetail.setPmsPrestationId(Integer.parseInt(request.get("pmsPrestationId").toString()));
        newVenteComptantDetail.setQte(Integer.parseInt(request.get("qte").toString()));
        newVenteComptantDetail.setPu(new BigDecimal(request.get("pu").toString()));        
        newVenteComptantDetail.setRemise(new BigDecimal(request.get("remise").toString()));
        newVenteComptantDetail.setCommission(new BigDecimal(request.get("commission").toString()));
        newVenteComptantDetail.setOrigine(venteComptantDetail.getOrigine());

        
        try {
            entityManager.persist(newVenteComptantDetail);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
        return newVenteComptantDetail;
    }
}
