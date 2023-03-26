/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsFacture;
import cloud.multimicro.mmc.Entity.TPmsFactureDetail;
import cloud.multimicro.mmc.Entity.TPosFacture;
import cloud.multimicro.mmc.Entity.TPosFactureDetail;
import cloud.multimicro.mmc.Entity.VPmsFactureDetail;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;


/**
 *
 * @author Naly
 */
@Stateless
public class InvoiceDao {
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Inject
    SettingDao settingDao;
    
    public List<TPmsFacture> getTpmsFacture() {
        List<TPmsFacture> facture = entityManager.createQuery(" FROM TPmsFacture  ").getResultList();
        return facture;
    }
    
     public TPmsFacture getNumFacture(String numInvoice)throws NoResultException {
        TPmsFacture result = (TPmsFacture) entityManager.createQuery("FROM TPmsFacture WHERE numero =:numInvoice")
                .setParameter("numInvoice", numInvoice)
                .getSingleResult();
        return result;
    }
     
    public List<TPmsFactureDetail> getInvoiceDetail(String numInvoice)throws NoResultException {
        List<TPmsFactureDetail> result = entityManager.createQuery("FROM TPmsFactureDetail WHERE pmsFactureNumero =:numInvoice")
            .setParameter("numInvoice", numInvoice)
            .getResultList();
        return result;

    }
    
    public void setTpmsFacture(TPmsFacture facture) throws CustomConstraintViolationException {
        try{
            facture.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            entityManager.persist(facture);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
     
    public List<TPmsFactureDetail> getTpmsFactureDetail() {
        List<TPmsFactureDetail> customer = entityManager.createQuery(" FROM TPmsFactureDetail  ").getResultList();
        return customer;
    }
    
    public void setTpmsFactureDetail(TPmsFactureDetail facture) throws CustomConstraintViolationException {
        try{
            facture.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            entityManager.persist(facture);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
     
    public List<TPosFacture> getTposFacture() {
        List<TPosFacture> customer = entityManager.createQuery(" FROM TPosFacture  ").getResultList();
        return customer;
    }
    
    public void setTposFacture(TPosFacture facture) throws CustomConstraintViolationException {
        try{
            facture.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            entityManager.persist(facture);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public List<TPosFactureDetail> getTposFactureDetail() {
        List<TPosFactureDetail> customer = entityManager.createQuery(" FROM TPosFactureDetail  ").getResultList();
        return customer;
    }
    
    public void setTposFactureDetail(TPosFactureDetail factureDetail) throws CustomConstraintViolationException {
        try{
            factureDetail.setDevise(settingDao.getSettingByKey("DEFAULT_CURRENCY").getValeur());
            entityManager.persist(factureDetail);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    /*public List<VPmsFactureDetail> getInvoiceDetail(String numInvoice)throws NoResultException {
        List<VPmsFactureDetail> result = entityManager.createQuery("FROM VPmsFactureDetail WHERE numFacture =:numInvoice")
            .setParameter("numInvoice", numInvoice)
            .getResultList();
        return result;

    }*/
    
    
    
}
