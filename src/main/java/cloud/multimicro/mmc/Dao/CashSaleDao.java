/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcTva;
import cloud.multimicro.mmc.Entity.TPmsArrhe;
import cloud.multimicro.mmc.Entity.TPmsPrestation;
import cloud.multimicro.mmc.Entity.TPmsVenteComptant;
import cloud.multimicro.mmc.Entity.TPmsVenteComptantDetail;
import cloud.multimicro.mmc.Entity.TPmsVenteComptantEncaissement;
import cloud.multimicro.mmc.Entity.TPosPrestation;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
    
    @Inject
    SettingDao settingDao;
    
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
        String dateLogicielle = settingDao.getSettingByKey("DATE_LOGICIELLE").getValeur();
        LocalDate dateLog = LocalDate.parse(dateLogicielle);
        try {
            cashSales.setDateNote(dateLog);
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
        List<TPmsVenteComptantDetail> cashSalesDetail = entityManager.createQuery("FROM TPmsVenteComptantDetail ")
                .getResultList();
        return cashSalesDetail;
    }
    
    public TPmsVenteComptantDetail getCashSalesDetailById(int id) {
        TPmsVenteComptantDetail cashSalesDetail = entityManager.find(TPmsVenteComptantDetail.class, id);
        return cashSalesDetail;
    }
    
    public void setCashSalesDetail(TPmsVenteComptantDetail cashSalesDetail, Integer pmsVenteComptantId) throws CustomConstraintViolationException {
        try {
            cashSalesDetail.setPmsVenteComptantId(pmsVenteComptantId);
            cashSalesDetail.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            cashSalesDetail.setTva(getMmcTvaFromPmsPrestationId(cashSalesDetail.getPmsPrestationId()).getValeur());
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
        venteComptantDetailUpdate.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
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
        newVenteComptantDetail.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
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
    
    private TMmcTva getMmcTvaFromPmsPrestationId(Integer pmsPrestationId) {
        String sql = "select tva from TPmsPrestation p join TMmcSousFamilleCa sf on p.mmcSousFamilleCaId = sf.id "+
                     "join TMmcTva tva on sf.mmcTvaId = tva.id "+
                     "where p.id = :id";
        TMmcTva tva = (TMmcTva) entityManager.createQuery(sql).setParameter("id", pmsPrestationId).getSingleResult();
        return tva;
    }
    
    //Vente comptant encaissement
    public List<TPmsVenteComptantEncaissement> getCashSalesEncaissement() {
        List<TPmsVenteComptantEncaissement> cashSalesEncaissement = entityManager.createQuery("FROM TPmsVenteComptantEncaissement")
                .getResultList();
        return cashSalesEncaissement;
    }
    
    public void setCashSalesEncaissement(TPmsVenteComptantEncaissement cashSalesEncaissement) throws CustomConstraintViolationException {
        try {
            cashSalesEncaissement.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            entityManager.persist(cashSalesEncaissement);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TPmsVenteComptantEncaissement getCashSalesEncaissementById(int id) {
        TPmsVenteComptantEncaissement cashSales = entityManager.find(TPmsVenteComptantEncaissement.class, id);
        return cashSales;
    }
    
}