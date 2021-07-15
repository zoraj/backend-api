/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TMmcDevise;
import cloud.multimicro.mmc.Entity.TMmcDeviseTauxChange;
import cloud.multimicro.mmc.Entity.TMmcFamilleCa;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author HERIZO
 */
@Stateless
@SuppressWarnings("unchecked")
public class DeviseDao {
    @PersistenceContext
    private EntityManager entityManager;
    
     //Client
    public List<TMmcDevise> getAll() {
        List<TMmcDevise> devise = entityManager.createQuery("FROM TMmcDevise where dateDeletion = null").getResultList();
        return devise;
    }
    
    public void setDevise(TMmcDevise devise) throws CustomConstraintViolationException {
        try{
        entityManager.persist(devise);
          } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TMmcDevise getByIdDevise(int id) {
        TMmcDevise customer = entityManager.find(TMmcDevise.class, id);
        return customer;
    }
    
    public void updateDevise(TMmcDevise devise) throws CustomConstraintViolationException {
        try{
        entityManager.merge(devise);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void deleteDevise(int id) throws CustomConstraintViolationException { 
         TMmcDevise devise = getByIdDevise(id);
         Date dateDel = new Date();
         devise.setDateDeletion(dateDel);
         try{
         entityManager.merge(devise);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    
    public List<TMmcDeviseTauxChange> getExchangeRate() {
        List<Object[]> exchangeRates = entityManager.createQuery("FROM TMmcDeviseTauxChange tauxChange "
                                                            + "LEFT JOIN TMmcDevise d ON  d.id = tauxChange.mmcDeviseId  "
                                                            + "LEFT JOIN TMmcDevise d ON  d.id = tauxChange.mmcDeviseReferenceId  ")
                                                .getResultList();
        List<TMmcDeviseTauxChange> result = new ArrayList<TMmcDeviseTauxChange>();

        // Retrieve the corresponding sub-family
        for (Object[] exchangeRate: exchangeRates) {
            if (exchangeRate.length > 1) {
                TMmcDeviseTauxChange tauxChange = (TMmcDeviseTauxChange) exchangeRate[0];
                TMmcDevise d1 = (TMmcDevise) exchangeRate[1];
                tauxChange.setMmcDeviseLibelle(d1.getLibelle());
                TMmcDevise d2 = (TMmcDevise) exchangeRate[2];
                tauxChange.setMmcDeviseReferenceLibelle(d2.getLibelle());
                result.add(tauxChange);
            }
        }        
        return result;
   }
    
    public void setExchangeRate(TMmcDeviseTauxChange exchangeRate) throws CustomConstraintViolationException {
        try{
        entityManager.persist(exchangeRate);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TMmcDeviseTauxChange getByIdExchangeRate(int id) {
        TMmcDeviseTauxChange exchangeRate = entityManager.find(TMmcDeviseTauxChange.class, id);
        return exchangeRate;
    }
    
    public void updateExchangeRate(TMmcDeviseTauxChange exchangeRate) throws CustomConstraintViolationException {
        try{
        entityManager.merge(exchangeRate);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public List<TMmcDeviseTauxChange> getExchangeRateByReferenceAndByDate(int id,String exchangeRateDate){ 
        List<Object[]> exchangeRates = entityManager.createQuery("FROM TMmcDeviseTauxChange tauxChange "
                                                            + "LEFT JOIN TMmcDevise d ON  d.id = tauxChange.mmcDeviseId  "
                                                            + "LEFT JOIN TMmcDevise d ON  d.id = tauxChange.mmcDeviseReferenceId  where tauxChange.dateTauxChange =:dateTauxChange and tauxChange.mmcDeviseReferenceId =:id")
                                                            .setParameter("dateTauxChange", exchangeRateDate)
                                                            .setParameter("id", id)
                                                            .getResultList();
        List<TMmcDeviseTauxChange> result = new ArrayList<TMmcDeviseTauxChange>();

        // Retrieve the corresponding sub-family
        for (Object[] exchangeRate: exchangeRates) {
            if (exchangeRate.length > 1) {
                TMmcDeviseTauxChange tauxChange = (TMmcDeviseTauxChange) exchangeRate[0];
                TMmcDevise d1 = (TMmcDevise) exchangeRate[1];
                tauxChange.setMmcDeviseLibelle(d1.getLibelle());
                TMmcDevise d2 = (TMmcDevise) exchangeRate[2];
                tauxChange.setMmcDeviseReferenceLibelle(d2.getLibelle());
                result.add(tauxChange);
            }
        }      
        return result;
    }
}
