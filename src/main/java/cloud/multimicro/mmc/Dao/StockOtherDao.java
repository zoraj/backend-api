/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloud.multimicro.mmc.Dao;

import cloud.multimicro.mmc.Entity.TPmsReservationStockAutre;
import cloud.multimicro.mmc.Entity.TPmsStockAutre;
import cloud.multimicro.mmc.Exception.CustomConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Tsiory-PC
 */
@Stateless
@SuppressWarnings("unchecked")
public class StockOtherDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<TPmsStockAutre> getAllStockOther() {
        List<TPmsStockAutre> stock = entityManager.createQuery("FROM TPmsStockAutre WHERE dateDeletion = null").getResultList();
        return stock;
    }
    
    public TPmsStockAutre getStockOtherId(int id) {
        TPmsStockAutre stock = entityManager.find(TPmsStockAutre.class, id);
        return stock;
    }
    
    public void setStockOther(TPmsStockAutre stock) throws CustomConstraintViolationException {
        try {
            entityManager.persist(stock);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TPmsStockAutre updateStockOther(TPmsStockAutre stock) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(stock);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void deleteStockOther(int id) {
        entityManager.createNativeQuery("UPDATE t_pms_stock_autre SET date_deletion = CURRENT_TIMESTAMP WHERE id=:id")              
             .setParameter("id", id)               
             .executeUpdate();
    }
    
    public List<TPmsReservationStockAutre> getAllResaStockOther() {
        List<TPmsReservationStockAutre> stockResa = entityManager.createQuery("FROM TPmsReservationStockAutre WHERE dateDeletion = null").getResultList();
        return stockResa;
    }
    
    public List<TPmsReservationStockAutre> getResaStockOtherByIdReservation(int idReservation) {
        List<TPmsReservationStockAutre> stockResa = entityManager.createQuery("FROM TPmsReservationStockAutre WHERE pmsReservationId =:idReservation")
                .setParameter("idReservation", idReservation) 
                .getResultList();
        return stockResa;
    }
    
    public void setResaStockOther(TPmsReservationStockAutre stockResa) throws CustomConstraintViolationException {
        try {
            entityManager.persist(stockResa);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public TPmsReservationStockAutre updateResaStockOther(TPmsReservationStockAutre stockResa) throws CustomConstraintViolationException {
        try {
            return entityManager.merge(stockResa);
        } catch (ConstraintViolationException ex) {
            throw new CustomConstraintViolationException(ex);
        }
    }
    
    public void deleteResaStockOther(int idReservation, int idStockAutre) {
        entityManager.createNativeQuery("UPDATE t_pms_reservation_stock_autre SET date_deletion = CURRENT_TIMESTAMP "
                + "WHERE pms_reservation_id=:idReservation AND pms_stock_autre_id=:idStockAutre")              
            .setParameter("idReservation", idReservation)
            .setParameter("idStockAutre", idStockAutre)
            .executeUpdate();
    }
    
}