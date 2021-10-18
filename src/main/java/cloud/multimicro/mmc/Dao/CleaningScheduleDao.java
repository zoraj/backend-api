/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsCalendrierNettoyage;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Naly
 */
@Stateless
@SuppressWarnings("unchecked")
public class CleaningScheduleDao {
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<TPmsCalendrierNettoyage> getAll() {
        List<TPmsCalendrierNettoyage> calendrierNettoyage = entityManager.createQuery("FROM TPmsCalendrierNettoyage where dateDeletion is null ORDER BY dateNettoyage").getResultList();
        return calendrierNettoyage;
    }
    
    public TPmsCalendrierNettoyage getById(Integer id) {
        TPmsCalendrierNettoyage calendrierNettoyage = entityManager.find(TPmsCalendrierNettoyage.class, id);
        return calendrierNettoyage;
    }
        
    public void setPmsCalendrierNettoyage(TPmsCalendrierNettoyage nettoyage) throws CustomConstraintViolationException{
        try {
            entityManager.persist(nettoyage);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TPmsCalendrierNettoyage updateCleaningSchedule(TPmsCalendrierNettoyage nettoyage) throws CustomConstraintViolationException{
        try {
         return entityManager.merge(nettoyage);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
      public void deleteCleaningSchedule(Integer id) throws CustomConstraintViolationException { 
         TPmsCalendrierNettoyage pmsCalendrierNettoyage = getById(id);
         LocalDateTime dateDel = LocalDateTime.now();
         pmsCalendrierNettoyage.setDateDeletion(dateDel);
         try {
         entityManager.merge(pmsCalendrierNettoyage);
         } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
}
