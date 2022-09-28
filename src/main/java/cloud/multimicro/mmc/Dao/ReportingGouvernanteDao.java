/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.THkElementReporting;
import cloud.multimicro.mmc.Entity.THkReportingChambre;
import cloud.multimicro.mmc.Entity.TMmcParametrage;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import cloud.multimicro.mmc.Exception.DataException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    

    public List<THkReportingChambre> getReportingChambre() {
        List<Object[]> mmcSFamilies = entityManager.createQuery(
                "FROM THkReportingChambre tc LEFT JOIN THkElementReporting te ON  tc.hkElementReportingId = te.id WHERE tc.dateDeletion = null ORDER BY tc.dateReporting")
                .getResultList();
        List<THkReportingChambre> result = new ArrayList<THkReportingChambre>();
        // Retrieve the corresponding sub-family
        for (Object[] mmcSFamilie : mmcSFamilies) {
            if (mmcSFamilie.length > 1) {
                THkReportingChambre tc = (THkReportingChambre) mmcSFamilie[0];
                result.add(tc);
            }
        }
        return result;
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
    
    public void setReportingChambre(THkReportingChambre reportingChambre) throws CustomConstraintViolationException {
        LocalTime currentTime = LocalTime.now();  
        TMmcParametrage parametrageDateLogicielle = entityManager.find(TMmcParametrage.class, "DATE_LOGICIELLE");
        LocalDate dateLogiciel = LocalDate.parse(parametrageDateLogicielle.getValeur());
        LocalDateTime dateTimeLogiciel = currentTime.atDate(dateLogiciel);   
        try {
            reportingChambre.setDateReporting(dateTimeLogiciel);
            entityManager.persist(reportingChambre);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public THkReportingChambre updateReportingChambre(THkReportingChambre reportingChambre) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(reportingChambre);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }

    public THkReportingChambre getReportingChambreById(int id) {
        THkReportingChambre mmcFamilies = entityManager.find(THkReportingChambre.class, id);
        return mmcFamilies;
    }
    
    public void deleteReportingChambre(int id) throws CustomConstraintViolationException {
        THkReportingChambre reportingChambre = getReportingChambreById(id);
        Date dateDel = new Date();
        reportingChambre.setDateDeletion(dateDel);
        try {
            entityManager.merge(reportingChambre);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
}
