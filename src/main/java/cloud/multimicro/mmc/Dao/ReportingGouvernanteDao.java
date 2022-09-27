/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.THkElementReporting;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author HERIZO-PC
 */
@Stateless
@SuppressWarnings("unchecked")
public class ReportingGouvernanteDao {
    
     @PersistenceContext
    private EntityManager entityManager;
    
    public List<THkElementReporting> getAllReportingGouvernante() {
        List<THkElementReporting> stock = entityManager.createQuery("FROM THkElementReporting WHERE dateDeletion = null").getResultList();
        return stock;
    }
    
    public THkElementReporting getReportingGouvernanteId(int id) {
        THkElementReporting stock = entityManager.find(THkElementReporting.class, id);
        return stock;
    }
    
    public void setReportingGouvernante(THkElementReporting stock) throws CustomConstraintViolationException {
        try {
            entityManager.persist(stock);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public THkElementReporting updateReportingGouvernante(THkElementReporting stock) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(stock);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void deleteReportingGouvernante(int id) {
        entityManager.createNativeQuery("UPDATE t_hk_element_reporting SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")              
             .setParameter("id", id)               
             .executeUpdate();
    }
}
